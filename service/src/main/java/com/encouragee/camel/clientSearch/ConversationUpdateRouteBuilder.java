package com.encouragee.camel.clientSearch;

import com.encouragee.camel.clientSearch.conversation.index.ConversationCalculator;
import com.encouragee.camel.clientSearch.conversation.index.ConversationCleaner;
import com.encouragee.camel.clientSearch.model.ConversationUpdate;
import com.encouragee.model.solr.ConversationDocument;
import com.zoomint.encourage.model.conversation.Conversation;
import com.zoomint.encourage.model.conversation.ConversationEvent;
import com.zoomint.encourage.model.conversation.ConversationList;
import com.zoomint.encourage.model.conversation.event.AnonymizeEvent;
import com.zoomint.encourage.model.conversation.event.EventList;
import com.zoomint.encourage.model.conversation.event.EventLookup;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.restlet.RestletOperationException;
import org.apache.solr.client.solrj.impl.HttpSolrClient.RemoteSolrException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Map.Entry;

import static com.encouragee.ConversationProperties.SOURCE_CALLREC;
import static com.encouragee.ConversationProperties.getSourceSystem;
import static com.encouragee.camel.clientSearch.conversation.index.ConversationCalculator.EVENT_ORDERING;
import static com.zoomint.encourage.common.camel.util.ExchangeUtils.reduce;
import static com.zoomint.encourage.model.conversation.ConversationEvent.EVENT_VERSIONING;
import static java.net.HttpURLConnection.HTTP_CONFLICT;
import static java.util.Collections.singletonList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static org.apache.camel.util.toolbox.AggregationStrategies.useOriginal;

@Component
@Profile("solr")
public class ConversationUpdateRouteBuilder extends RouteBuilder {

	public static final String URI_ADD_EVENT_TO_CONVERSATION = "direct:convAddEvent";
	public static final String URI_UPDATE_CONVERSATIONS = "direct:convUpdate";

	private static final String URI_TRY_ADD_EVENT = "direct:convTryAddEvent";
	private static final String URI_UPDATE_EVENTS = "direct:convUpdateEvents";
	private static final String URI_DELETE_INVALID_EVENTS = "direct:convDeleteEvents";
	private static final String URI_REPORT_DELETED = "direct:convReportDeleted";
	private static final String URI_CHECK_CONVERSATION_PROTECTION = "direct:convCheckProtection";

	private final ConversationModifier modifier;
	private final ConversationCleaner cleaner;
	private final ConversationCalculator calculator;

	public ConversationUpdateRouteBuilder(
			ConversationModifier modifier,
			ConversationCleaner cleaner,
			ConversationCalculator calculator) {

		this.modifier = modifier;
		this.cleaner = cleaner;
		this.calculator = calculator;
	}

	@Override
	public void configure() {
		errorHandler(noErrorHandler()); // propagate exceptions back to original route

		from(URI_ADD_EVENT_TO_CONVERSATION).routeId("convAddEvent")
				.errorHandler(defaultErrorHandler())
				.onException(RemoteSolrException.class).onWhen(this::isOptimisticLockFail).maximumRedeliveries(5).redeliveryDelay(0L).end()
				.onException(RestletOperationException.class).onWhen(this::isOptimisticLockFail).maximumRedeliveries(5).redeliveryDelay(0L).end()
				.enrich(URI_TRY_ADD_EVENT) // via enrich to clone original exchange on each attempt
				.setBody().body(ConversationUpdate.class, update -> update.getConversations().get(0))
				.process().body(Conversation.class, calculator::calculateBusinessFields)
		;

		from(URI_TRY_ADD_EVENT).routeId("convTryAddEvent")
				.to(URI_CHECK_CONVERSATION_PROTECTION)
				.to(EnrichRouteBuilder.URI_LOAD_NEW_EVENT_DATA)
				.setBody().exchange(this::createUserModification)
				.to(URI_UPDATE_CONVERSATIONS)
		;

		from(URI_UPDATE_CONVERSATIONS).routeId("convUpdate")
				.convertBodyTo(ConversationUpdate.class)
				// skip everything if empty
				.filter().body(ConversationUpdate.class, update -> !update.getConversations().isEmpty())

				.to(ConversationIndexingRouteBuilder.URI_PREPARE_UPDATE)
				.enrichWith(ConversationSearchRouteBuilder.URI_LOOKUP_EVENTS).body(ConversationUpdate.class, EventList.class, ConversationUpdate::addEvents)

				.process().body(ConversationUpdate.class, this::recalculateConversations)

				// update event sources if necessary
				.filter().body(ConversationUpdate.class, update -> !update.getChangedEvents().isEmpty())
					.enrichWith(URI_UPDATE_EVENTS).body(ConversationUpdate.class, EventList.class, this::replaceChangedEvents)
				.end()

				.enrich(URI_DELETE_INVALID_EVENTS, useOriginal())
				.enrich(URI_REPORT_DELETED, useOriginal())
				.to(ConversationIndexingRouteBuilder.URI_PERFORM_UPDATE)
		;

		from(URI_UPDATE_EVENTS).routeId("convUpdateEvents")
				.split().body(ConversationUpdate.class, update -> splitBySourceSystem(update.getChangedEvents()))
					.aggregationStrategy(reduce(EventList.class, this::combineEventLists))
					.parallelProcessing() // contact source systems at the same time
					.stopOnException()
					.choice().when().body(Entry.class, entry -> SOURCE_CALLREC.equals(entry.getKey()))
						.setBody().body(Entry.class, Entry::getValue)
						.to(ZQMConnectorRouteBuilder.URI_UPDATE_EVENTS)
					.otherwise()
						.setBody().body(Entry.class, Entry::getValue)
						.to(DataAccessRouteBuilder.URI_UPDATE_EVENTS)
					.end()
				.end()
		;

		from(URI_DELETE_INVALID_EVENTS).routeId("convDeleteEvents")
				.setBody().body(ConversationUpdate.class, this::getDeletedEventLookup)
				.to(DataAccessRouteBuilder.URI_DELETE_EVENTS)
		;

		from(URI_REPORT_DELETED).routeId("convReportDeleted")
				.setBody().body(ConversationUpdate.class, this::getDeletedConversations)
				.to(RabbitMQRouteBuilder.URI_CONVERSATIONS_DELETED)
		;

		from(URI_CHECK_CONVERSATION_PROTECTION).routeId("convCheckProtection")
				.filter().body(ConversationEvent.class, this::isSubjectToProtection)
				.enrich(SearchTemplateRouteBuilder.URI_ENSURE_CONVERSATION_NOT_PROTECTED, useOriginal())
		;
	}

	private ConversationUpdate createUserModification(Exchange exchange) {
		String conversationId = exchange.getProperty("conversationId", String.class);
		ConversationEvent event = exchange.getIn().getBody(ConversationEvent.class);

		ConversationUpdate update = new ConversationUpdate(singletonList(
				Conversation.builder()
						.conversationId(conversationId)
						.event(event)
						.build()));
		update.setFailOnMissing(true);
		update.setModifications(conversation -> update.getChangedEvents().addAll(modifier.addModifyingEvent(event, conversation)));
		return update;
	}

	/**
	 * Conversations are recalculated based on new and existing events.
	 *
	 * @param update the update with conversations to recalculate, as well as loaded referenced documents and events
	 */
	private void recalculateConversations(ConversationUpdate update) {
		log.debug("Recalculating {} conversations", update.getConversations().size());

		// add events from referenced conversations - ignoring duplicates as necessary
		update.getConversations().forEach(conversation -> {
			log.trace("Recalculating conversation {}", conversation.getConversationId());

			// start with events already in the conversation, if any
			Map<String, ConversationEvent> allEvents = conversation.getEvents().stream()
					.map(ConversationEvent::getEventId)
					.map(update.getEventById()::get) // use the looked up event, ignoring what was actually received
					.filter(Objects::nonNull) // skip events that no longer exist
					.collect(toMap(ConversationEvent::getEventId, identity(), EVENT_VERSIONING::max, LinkedHashMap::new));

			// add documents based on previousIds - recursively, if necessary
			LinkedList<String> processQueue = new LinkedList<>();
			processQueue.add(conversation.getConversationId());
			processQueue.addAll(conversation.getPreviousIds());
			Set<String> mergedIds = new HashSet<>();
			while (!processQueue.isEmpty()) {
				String mergedId = processQueue.removeFirst();
				if (!mergedIds.add(mergedId)) {
					continue; // skip duplicate
				}

				log.trace("Checking pre-fetched document: {}", mergedId);
				ConversationDocument mergedDocument = update.getDocumentById().get(mergedId);
				if (mergedDocument == null) {
					log.trace("Document not pre-fetched: {}", mergedId);
					continue; // skip missing
				}

				// add events from the merged document, if any
				if (!mergedDocument.getEventIds().isEmpty()) {
					log.trace("Checking {} events from document {}", mergedDocument.getEventIds().size(), mergedDocument.getConversationId());
					mergedDocument.getEventIds().stream()
							.map(update.getEventById()::get)
							.filter(Objects::nonNull)
							// resolve each duplicate via merge priority
							.forEach(event -> allEvents.merge(event.getEventId(), event, EVENT_VERSIONING::max));
				}

				// check for redirect
				if (mergedDocument.getNewId() != null) {
					log.trace("Document {} redirects to: {}", mergedDocument.getConversationId(), mergedDocument.getNewId());
					processQueue.add(mergedDocument.getNewId());
				}

				// enqueue other previous IDs, if any
				processQueue.addAll(mergedDocument.getPreviousIds());
			}

			conversation.setPreviousIds(mergedIds.stream()
					.filter(processedId -> processedId != null && !processedId.equals(conversation.getConversationId()))
					.collect(toList()));

			log.trace("Recalculated conversation events: {} -> {}", conversation.getEvents().size(), allEvents.size());
			conversation.setEvents(allEvents.values().stream().sorted(EVENT_ORDERING).collect(toList()));

			// apply all modifications to the final events
			if (update.getModifications() != null) {
				log.trace("Applying modifications to conversation {}", conversation.getConversationId());
				update.getModifications().accept(conversation);
			}

			cleanupConversation(update, conversation);
		});
	}

	private void cleanupConversation(ConversationUpdate update, Conversation conversation) {
		log.trace("Performing cleanup of conversation {}", conversation.getConversationId());
		List<ConversationEvent> removedEvents = cleaner.removeInvalidEvents(conversation.getEvents());
		update.getChangedEvents().removeAll(removedEvents);
		update.getDeletedEvents().addAll(removedEvents);
	}

	private EventLookup getDeletedEventLookup(ConversationUpdate update) {
		return EventLookup.builder()
				.eventIds(update.getDeletedEvents().stream()
						.map(ConversationEvent::getEventId)
						.collect(toSet()))
				.build();
	}

	private ConversationList getDeletedConversations(ConversationUpdate update) {
		return update.getConversations().stream()
				.filter(conv -> conv.getEvents().isEmpty())
				.collect(collectingAndThen(toList(), ConversationList::new));
	}

	private boolean isSubjectToProtection(ConversationEvent event) {
		// currently only AnonymizeEvent triggers protection
		return event instanceof AnonymizeEvent;
	}

	private Set<Entry<String, EventList>> splitBySourceSystem(Set<ConversationEvent> changedEvents) {
		return changedEvents.stream()
				.collect(groupingBy(event -> getSourceSystem(event.getEventId()),
						collectingAndThen(toList(), EventList::new)))
				.entrySet();
	}

	private ConversationUpdate replaceChangedEvents(ConversationUpdate update, EventList eventList) {
		log.debug("Replacing {} events with {} updated versions", update.getChangedEvents().size(), eventList.getEvents().size());
		// replace all sent events with the returned versions of the events (some sent events might be missing now!)
		update.getChangedEvents().forEach(event -> update.getEventById().remove(event.getEventId()));
		eventList.getEvents().forEach(event -> update.getEventById().put(event.getEventId(), event));

		// update conversations
		log.debug("Updating {} conversations based on replaced events", update.getConversations().size());
		update.getConversations().forEach(conversation -> {
			log.trace("Updating events for conversation {}", conversation.getConversationId());
			log.trace("Number of events before update: {}", conversation.getEvents().size());
			// update events in conversation (removing any event that no longer exists in the source)
			conversation.setEvents(
					conversation.getEvents().stream()
							.map(event -> update.getEventById().get(event.getEventId()))
							.filter(Objects::nonNull)
							.collect(toList()));
			// cleanup conversation in case it is now changed
			cleanupConversation(update, conversation);
			log.trace("Number of events after update: {}", conversation.getEvents().size());
		});

		return update;
	}

	private EventList combineEventLists(EventList one, EventList two) {
		one.getEvents().addAll(two.getEvents());
		return one;
	}

	private boolean isOptimisticLockFail(Exchange exchange) {
		RemoteSolrException solrException = exchange.getException(RemoteSolrException.class);
		if (solrException != null) {
			return solrException.code() == HTTP_CONFLICT;
		}
		RestletOperationException restletException = exchange.getException(RestletOperationException.class);
		if (restletException != null) {
			return restletException.getStatusCode() == HTTP_CONFLICT;
		}
		return false;
	}
}

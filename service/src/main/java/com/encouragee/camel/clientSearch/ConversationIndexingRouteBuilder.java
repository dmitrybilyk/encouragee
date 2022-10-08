
package com.encouragee.camel.clientSearch;

import com.encouragee.camel.clientSearch.conversation.converter.ConversationDocumentConverter;
import com.encouragee.camel.clientSearch.model.ConversationUpdate;
import com.encouragee.camel.clientSearch.repository.ConversationSearchRepository;
import com.encouragee.model.solr.ConversationDocument;
import com.google.common.collect.Sets;
import com.zoomint.encourage.common.exceptions.ResourceNotFoundException;
import com.zoomint.encourage.model.conversation.Conversation;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Stream;

import static com.encouragee.camel.clientSearch.repository.ConversationSearchRepository.PARAM_MAX_LENGTH;
import static com.encouragee.model.solr.ConversationDocument.CURRENT_SCHEMA_VERSION;
import static java.lang.Math.min;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;
import static org.apache.camel.LoggingLevel.DEBUG;

@Component
public class ConversationIndexingRouteBuilder extends RouteBuilder {
	public static final String URI_PREPARE_UPDATE = "direct:indexConvPrepare";
	public static final String URI_PERFORM_UPDATE = "direct:indexConvPerform";
	public static final String URI_DELETE_ALL = "direct:indexConvDeleteAll";

	private final ConversationSearchRepository repository;
	private final ConversationDocumentConverter converter;

	public ConversationIndexingRouteBuilder(ConversationSearchRepository repository, ConversationDocumentConverter converter) {
		this.repository = repository;
		this.converter = converter;
	}

	@Override
	public void configure() {
		errorHandler(noErrorHandler()); // propagate exceptions back to original route

		from(URI_DELETE_ALL).routeId("indexConvDeleteAll")
				.log(DEBUG, "Deleting conversations")
				.process(exchange -> repository.deleteAll())
		;

		from(URI_PREPARE_UPDATE).routeId("indexConvPrepare")
				.convertBodyTo(ConversationUpdate.class)
				.process().body(ConversationUpdate.class, this::verifyConversations)
				.process().body(ConversationUpdate.class, this::loadConversationDocuments)
		;

		from(URI_PERFORM_UPDATE).routeId("indexConvPerform")
				.to(EnrichRouteBuilder.URI_ENRICH_UPDATE)
				.process().body(ConversationUpdate.class, this::indexConversations)
		;
	}

	private void verifyConversations(ConversationUpdate update) {
		update.getConversations().stream()
			.peek(conversation -> Assert.hasText(conversation.getConversationId(), "Conversation must have conversationId"))
			.flatMap(conversation -> conversation.getEvents().stream())
			.forEach(event -> Assert.hasText(event.getEventId(), "Event must have eventId"));
	}

	private void loadConversationDocuments(ConversationUpdate update) {
		log.debug("Checking documents to load for update of {} conversations (with {} documents already loaded)",
				update.getConversations().size(), update.getDocumentById().size());

		// separate set ensures only one attempt is made to load each ID
		Set<String> processedIds = new HashSet<>(update.getDocumentById().keySet());

		List<String> loadQueue = Stream.of(
				// load all previousIds
				update.getDocumentById().values().stream()
						.flatMap(doc -> doc.getPreviousIds().stream()),
				// load all conversations
				update.getConversations().stream()
						.map(Conversation::getConversationId),
				// load all conversations' previousIds
				update.getConversations().stream()
						.flatMap(conversation -> conversation.getPreviousIds().stream())
		)
				.flatMap(identity())
				.filter(conversationId -> !processedIds.contains(conversationId))
				.distinct()
				.collect(toList());

		while (!loadQueue.isEmpty()) {
			int batchSize = min(loadQueue.size(), PARAM_MAX_LENGTH);
			List<String> loadBatch = loadQueue.subList(0, batchSize);
			log.debug("Loading {} additional documents", loadBatch.size());
			log.trace("Loading the following documents: {}", loadBatch);

			// prevent further attempts to load these IDs even if referenced again by subsequent documents
			processedIds.addAll(loadBatch);

			repository.findByConversationIdIn(loadBatch).stream()
					// add to existing before checking for other IDs
					.peek(doc -> update.getDocumentById().put(doc.getConversationId(), doc))
					.peek(doc -> log.trace("Loaded document: {} (version {})", doc.getConversationId(), doc.getVersion()))
					// collect newIds and previousIds to maybe load them too
					.flatMap(document -> Stream.concat(
							document.getNewId() != null ? Stream.of(document.getNewId()) : Stream.empty(),
							document.getPreviousIds().stream()))
					.filter(conversationId -> !processedIds.contains(conversationId))
					.forEach(loadQueue::add);

			// remove the batch from the queue
			loadQueue.subList(0, batchSize).clear();
		}

		Set<String> missingIds = Sets.difference(processedIds, update.getDocumentById().keySet());
		if (!missingIds.isEmpty()) {
			if (update.isFailOnMissing()) {
				throw new ResourceNotFoundException("Conversation does not exist: " + String.join(", ", missingIds));
			}
			log.debug("{} documents were not found (will be created)", missingIds.size());
			log.trace("Documents that were not found (will be created): {}", missingIds);
		}
	}

	private void indexConversations(ConversationUpdate update) {
		long start = System.currentTimeMillis();
		log.debug("Preparing to index {} conversations", update.getConversations().size());

		// recalculate documents based on the recalculated state of the conversations
		Map<String, ConversationDocument> updates = new HashMap<>();
		for (Conversation conversation : update.getConversations()) {
			// ensure old IDs are redirecting to this document
			conversation.getPreviousIds().forEach(previousId -> {
				ConversationDocument existing = update.getDocumentById().get(previousId);
				ConversationDocument redirect = new ConversationDocument();
				redirect.setConversationId(previousId);
				redirect.setSchemaVersion(CURRENT_SCHEMA_VERSION);
				redirect.setVersion(existing == null ? -1L : existing.getVersion());
				redirect.setNewId(conversation.getConversationId());
				updates.put(redirect.getConversationId(), redirect);
				log.trace("Conversation will be a redirect: {} (version {}) -> {}",
						redirect.getConversationId(), redirect.getVersion(), redirect.getNewId());
			});

			// recalculate the document based on all active events
			ConversationDocument newDocument = converter.convertToDocument(conversation);
			newDocument.getGroupUUIDs().addAll(update.getGroupIdsForMemberUserIds(newDocument.getUserUUIDs()));

			// must update existing, if any; must be new if no existing
			newDocument.setVersion(Optional.ofNullable(update.getDocumentById().get(conversation.getConversationId()))
					.map(ConversationDocument::getVersion)
					.orElse(-1L));

			updates.put(newDocument.getConversationId(), newDocument);
			log.trace("Conversation will be updated: {} (version {}) with {} eventIds",
					newDocument.getConversationId(), newDocument.getVersion(), newDocument.getEventIds().size());
		}

		Map<Boolean, List<ConversationDocument>> emptyOrNot = updates.values().stream().collect(partitioningBy(this::isEmptyDocument));
		List<ConversationDocument> toDelete = emptyOrNot.get(Boolean.TRUE);
		List<ConversationDocument> toSave = emptyOrNot.get(Boolean.FALSE).stream()
				.filter(doc -> isModifiedDocument(doc, update.getDocumentById())) // only save if something has changed
				.collect(toList());

		if (!toSave.isEmpty()) {
			log.debug("Saving {} conversations to index ({}ms)", toSave.size(), System.currentTimeMillis() - start);
			repository.saveAll(toSave);
		}

		if (!toDelete.isEmpty()) {
			log.debug("Deleting {} conversations from index ({}ms)", toDelete.size(), System.currentTimeMillis() - start);
			repository.deleteAll(toDelete);
		}

		log.info("Indexed {} conversations ({}ms)", update.getConversations().size(), System.currentTimeMillis() - start);
	}

	private boolean isModifiedDocument(ConversationDocument newDocument, Map<String, ConversationDocument> existingDocuments) {
		return newDocument.getVersion() == -1L
				|| !newDocument.equals(existingDocuments.get(newDocument.getConversationId()));
	}

	private boolean isEmptyDocument(ConversationDocument document) {
		return document.getEventIds().isEmpty()
				&& document.getNewId() == null;
	}
}

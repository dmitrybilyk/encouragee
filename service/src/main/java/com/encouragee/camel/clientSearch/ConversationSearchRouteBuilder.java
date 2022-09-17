package com.encouragee.camel.clientSearch;

import com.encouragee.ConversationProperties;
import com.encouragee.camel.clientSearch.conversation.Conversation;
import com.encouragee.camel.clientSearch.conversation.converter.ConversationRestriction;
import com.encouragee.camel.clientSearch.conversation.index.ConversationCalculator;
import com.encouragee.camel.clientSearch.model.*;
import com.encouragee.camel.clientSearch.repository.ConversationSearchRepository;
import com.google.common.base.Splitter;
import com.zoomint.encourage.common.model.search.SearchTemplate;
import com.zoomint.encourage.common.model.search.SearchTemplateList;
import com.zoomint.encourage.model.conversation.ConversationEvent;
import com.zoomint.encourage.model.conversation.event.EventList;
import com.zoomint.encourage.model.conversation.event.EventLookup;
import com.zoomint.encourage.model.search.ClientConversationSearch;
import com.zoomint.encourage.model.search.ClientConversationSearchConverter;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Predicate;

import static com.encouragee.ConversationProperties.SOURCE_CALLREC;
import static com.encouragee.camel.clientSearch.DataAccessRouteBuilder.URI_GET_SAVED_FILTERS;
import static com.zoomint.encourage.common.camel.EncourageCamelConstants.HTTP_QUERY_MULTIMAP;
import static com.zoomint.encourage.common.camel.util.ExchangeUtils.reduce;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyMap;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static org.apache.camel.util.toolbox.AggregationStrategies.flexible;
import static org.apache.camel.util.toolbox.AggregationStrategies.useOriginal;
import static org.springframework.data.domain.Sort.unsorted;

@Component
public class ConversationSearchRouteBuilder extends RouteBuilder {

	public static final String URI_SEARCH_CONVERSATIONS = "direct:convSearchClient";
	public static final String URI_GET_CONVERSATION = "direct:convSearchById";

	public static final String URI_LOOKUP_EVENTS = "direct:convLookupEvents";
	private static final String URI_ENRICH_PERMISSION_FILTER = "direct:convPermissionFilter";

	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_SIZE = 20;
	private static final Sort DEFAULT_SORT = unsorted();

	private static final String QUERY_TIMESTAMP = "clientTimestamp";
	private static final String PROP_PERMISSIONS = "permissions";
	private static final String USERS_ASSIGNED_FILTERS = "usersAssignedFilters";
	private static final String USER_ID = "userId";

	private final ClientConversationSearchConverter converter;
	private final ConversationSearchRepository repository;
	private final ConversationRestriction restriction;
	private final ConversationCalculator calculator;
	private final Splitter sortParamSplitter = Splitter.on(",").limit(2).trimResults();

	public ConversationSearchRouteBuilder(
			ClientConversationSearchConverter converter,
			ConversationSearchRepository repository,
			ConversationRestriction restriction,
			ConversationCalculator calculator) {

		this.converter = converter;
		this.repository = repository;
		this.restriction = restriction;
		this.calculator = calculator;
	}

	@Override
	public void configure() {
		errorHandler(noErrorHandler()); // propagate exceptions back to original route

		from(URI_SEARCH_CONVERSATIONS).routeId("convSearchClient")
				.convertBodyTo(ClientConversationSearch.class)
				.process().body(ClientConversationSearch.class, converter::updateConversationSearch)
				.setProperty(PROP_PERMISSIONS).body(ClientConversationSearch.class, this::getConversationSearchPermissions)
//				.multicast()
//					.parallelProcessing()
//					.aggregationStrategy(useOriginal())
//					.stopOnException()
//					.to(URI_ENRICH_PERMISSION_FILTER, EnrichRouteBuilder.URI_ENRICH_SEARCH)
//				.end()
//				.setHeader(USER_ID, simple("${body?.onBehalfOf?.userId}"))
//				.enrich(URI_GET_SAVED_FILTERS, flexible().storeInProperty(USERS_ASSIGNED_FILTERS))
				.setBody(this::findConversations)
//				.enrichWith(URI_LOOKUP_EVENTS).exchange(this::getCombinedResults)
//				.to(EnrichRouteBuilder.URI_ENRICH_CONVERSATIONS)
		;

//		from(URI_GET_CONVERSATION).routeId("convSearchById")
//				.setBody(exchangeProperty("conversationId"))
//				.setBody().body(String.class, this::getEventLookupForConversationId)
//				.to(URI_LOOKUP_EVENTS)
//				.setBody().exchange(exchange ->
//						Conversation.builder()
//								.conversationId(exchange.getProperty("conversationId", String.class))
//								.events(exchange.getIn().getBody(EventList.class).getEvents())
//								.build())
//				.to(EnrichRouteBuilder.URI_ENRICH_CONVERSATION)
//				.process().body(Conversation.class, calculator::calculateBusinessFields)
//		;
//
		from(URI_LOOKUP_EVENTS).routeId("convLookupEvents")
				.convertBodyTo(EventLookup.class)
				.split().body(EventLookup.class, this::splitBySourceSystem)
					.aggregationStrategy(reduce(EventList.class, this::combineEventLists))
					.parallelProcessing() // contact source systems at the same time
					.stopOnException()
					.choice().when().body(Map.Entry.class, entry -> SOURCE_CALLREC.equals(entry.getKey()))
						.setBody().body(Map.Entry.class, Map.Entry::getValue)
						.to(ZQMConnectorRouteBuilder.URI_LOOKUP_EVENTS)
					.otherwise()
						.setBody().body(Map.Entry.class, Map.Entry::getValue)
						.to(DataAccessRouteBuilder.URI_LOOKUP_EVENTS)
					.end()
				.end()
		;
//
		from(URI_ENRICH_PERMISSION_FILTER).routeId("convPermissionFilter")
				.setBody().exchangeProperty(PROP_PERMISSIONS)
				.to(EnrichRouteBuilder.URI_ENRICH_SEARCH_PERMISSIONS)
		;
	}

	private ConversationSearchPermissions getConversationSearchPermissions(ClientConversationSearch search) {
		if (search.getOnBehalfOf() == null) {
			return ConversationSearchPermissions.builder().allowAll(true).build(); // unrestricted search
		}
		return restriction.getConversationSearchPermissions(search.getOnBehalfOf());
	}
//
//	private EventLookup getEventLookupForConversationId(String conversationId) {
//		ConversationDocument document = repository.findById(conversationId)
//				.orElseThrow(() -> new ResourceNotFoundException("Conversation does not exist: " + conversationId));
//		if (document.getNewId() != null) {
//			return getEventLookupForConversationId(document.getNewId());
//		}
//		return EventLookup.builder().eventIds(document.getEventIds()).build();
//	}
//
	@SuppressWarnings("unchecked")
	private Exchange getCombinedResults(Exchange resultsExchange, Exchange lookupExchange) {
		EncourageSolrResultPage<ConversationDocument> results = resultsExchange.getIn().getBody(EncourageSolrResultPage.class);
		log.debug("Processing {} search results", results.getContent().size());

		ConversationSearchPermissions permissions = resultsExchange.getProperty(PROP_PERMISSIONS, ConversationSearchPermissions.class);
		Predicate<ConversationEvent> filter = permissions.isAllowAll() ? null : permissions.getEventFilter(); // ignore filter if allowAll
		log.debug("Using event filter: {}", filter);

		Map<String, ConversationEvent> eventMap = Optional.ofNullable(lookupExchange.getIn().getBody(EventList.class))
				.map(list -> list.getEvents().stream()
						.filter(filter != null ? filter : event -> true)
						.collect(toMap(ConversationEvent::getEventId, identity())))
				.orElse(emptyMap());

		log.debug("Populating conversations based on {} looked up and filtered events", eventMap.size());
		List<Conversation> conversations = results.stream()
				.peek(document -> log.trace("Creating conversation based on document: {}", document))
				.map(document -> Conversation.builder()
						.conversationId(document.getConversationId())
						.previousIds(document.getPreviousIds())
						.events(document.getEventIds().stream()
								.peek(eventId -> log.trace("Adding event with ID: {}", eventId))
								.map(eventMap::get)
								.peek(event -> log.trace("Adding event: {}", event))
								.filter(Objects::nonNull)
								.collect(toList()))
						.build())
				.peek(conversation -> log.trace("Recalculating conversation: {}", conversation))
				.peek(calculator::calculateBusinessFields)
				.peek(conversation -> log.trace("Added conversation: {}", conversation))
				.collect(toList());

		log.debug("Applying {} highlights", results.getHighlighted().size());
		List<ConversationsPage.Highlighting> highlighting = results.getHighlighted().stream()
				.filter(docHighlights -> !docHighlights.getHighlights().isEmpty())
				.map(docHighlights -> new ConversationsPage.Highlighting(
						new Conversation(docHighlights.getEntity().getConversationId()),
						docHighlights.getHighlights()
								.stream()
								.map(highlight -> new ConversationsPage.Highlight(new ConversationsPage.Field(highlight.getField().getName()), highlight.getSnipplets()))
								.collect(toList())))
				.collect(toList());

		resultsExchange.getIn().setBody(new ConversationsPage(
				new ConversationsPage.Embedded(conversations),
				new HalPage.PageMetadata(
						results.getSize(),
						results.getTotalElements(),
						results.getTotalPages(),
						results.getNumber(),
						results.getCursorMark()),
				highlighting));
		return resultsExchange;
	}

	private EncourageSolrResultPage<ConversationDocument> findConversations(final Exchange exchange) {
		final MultivaluedMap<String, String> queryParameters = getQueryParameters(exchange);
		final ZonedDateTime clientTimestamp = Optional.ofNullable(queryParameters.getFirst(QUERY_TIMESTAMP))
				.map(ZonedDateTime::parse)
				.orElseGet(ZonedDateTime::now);
		final String cursorMark = queryParameters.getFirst("cursorMark");
		final PageRequest pageRequest = getPageRequest(queryParameters);

		final ClientConversationSearch search = exchange.getIn().getBody(ClientConversationSearch.class);
		log.debug("Executing search: {}", search);

		final ConversationSearchPermissions permissions = exchange.getProperty(PROP_PERMISSIONS, ConversationSearchPermissions.class);
		log.debug("Using permissions: {}", permissions);

		final SearchTemplateList usersAssignedFilters = exchange.getProperty(USERS_ASSIGNED_FILTERS, SearchTemplateList.class);

		EncourageSolrResultPage<ConversationDocument> results;
		if (usersAssignedFilters != null) {
			List<ClientConversationSearch> clientConversationSearches = usersAssignedFilters.getSearchTemplates().stream()
					.map(SearchTemplate::getConversationSearch).collect(toList());
			results = repository.findConversations(
					search,
					permissions,
					clientTimestamp,
					cursorMark,
					pageRequest,
					clientConversationSearches);
		} else {
			results = repository.findConversations(search, permissions, clientTimestamp, cursorMark, pageRequest);
		}

		log.debug("Found {} conversation documents", results.getContent().size());
		return results;
	}

	private Set<Map.Entry<String, EventLookup>> splitBySourceSystem(EventLookup eventLookup) {
		return eventLookup.getEventIds().stream()
				.collect(groupingBy(ConversationProperties::getSourceSystem,
						collectingAndThen(toList(), eventIds -> EventLookup.builder().eventIds(eventIds).build())))
				.entrySet();
	}

	private EventList combineEventLists(EventList one, EventList two) {
		one.getEvents().addAll(two.getEvents());
		return one;
	}

	@NotNull
	private PageRequest getPageRequest(MultivaluedMap<String, String> queryParameters) {
		return PageRequest.of(
				Optional.ofNullable(queryParameters.getFirst("page"))
						.map(Integer::valueOf)
						.orElse(DEFAULT_PAGE),
				Optional.ofNullable(queryParameters.getFirst("size"))
						.map(Integer::valueOf)
						.orElse(DEFAULT_SIZE),
				Optional.ofNullable(queryParameters.get("sort"))
						.map(sort -> Sort.by(sort.stream()
								.map(Object::toString)
								.map(param -> UriUtils.decode(param, UTF_8))
								.map(sortParamSplitter::splitToList)
								.filter(parts -> !parts.isEmpty()) // ignore empty sort param
								.map(parts -> new Order(
										parts.size() > 1 ? Direction.fromString(parts.get(1)) : Sort.DEFAULT_DIRECTION,
										parts.get(0)))
								.collect(toList())))
						.orElse(DEFAULT_SORT));
	}

	@SuppressWarnings("unchecked")
	private MultivaluedMap<String, String> getQueryParameters(Exchange exchange) {
		return (MultivaluedMap<String, String>) exchange.getProperty(HTTP_QUERY_MULTIMAP, new MultivaluedHashMap<>(), MultivaluedMap.class);
	}
}

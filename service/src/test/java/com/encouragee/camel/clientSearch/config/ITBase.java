package com.encouragee.camel.clientSearch.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.zoomint.encourage.common.model.label.LabelList;
import com.zoomint.encourage.common.model.label.LabelLookup;
import com.zoomint.encourage.common.model.search.SearchTemplateList;
import com.zoomint.encourage.common.model.settings.EncourageSettings;
import com.zoomint.encourage.common.model.user.Activity;
import com.zoomint.encourage.model.conversation.Conversation;
import com.zoomint.encourage.model.conversation.ConversationEvent;
import com.zoomint.encourage.model.conversation.ConversationList;
import com.zoomint.encourage.model.conversation.ConversationResource;
import com.zoomint.encourage.model.conversation.event.EventList;
import com.zoomint.encourage.model.conversation.event.EventLookup;
import com.zoomint.encourage.wiremock.RequestNotifier;
import com.zoomint.keycloak.lib.helper.core.RealmDtoAdapter;
import com.zoomint.keycloak.provider.api.dto.Role;
import com.zoomint.keycloak.provider.api.dto.User;
import io.restassured.specification.ResponseSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.assertj.core.api.Condition;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;

import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Stream;

import static com.encouragee.ConversationProperties.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.zoomint.encourage.common.exceptions.ErrorCode.CONVERSATION_IS_PROTECTED;
import static com.zoomint.encourage.common.exceptions.ErrorCode.UNKNOWN;
import static io.restassured.RestAssured.expect;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CONFLICT;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@ActiveProfiles("integration-tests")
@ContextHierarchy(@ContextConfiguration(classes = ITConfiguration.class))
@Slf4j
public abstract class ITBase extends AbstractTestNGSpringContextTests {

	protected static final Comparator<User> BASIC_USER_COMPARATOR = Comparator.comparing(User::getUserId).thenComparing(User::getUsername);

	@Autowired
	protected WireMockServer dataAccess;
	@Autowired
	protected WireMockServer zqmConnector;
	@Autowired
	protected WireMockServer scheduler;
	@Autowired
	protected RequestNotifier notifier;
	@Autowired
	protected ITHelper itHelper;
	@Autowired
	protected SolrClient solrClient;
	@Autowired
	protected AmqpAdmin rabbit;
	@Autowired
	protected RabbitTemplate rabbitTemplate;
	@Autowired
	protected ObjectMapper objectMapper;
	@Autowired
	protected RealmDtoAdapter keycloakRealm;

	protected URL reportUrl;
	protected Instant start;
	protected String prefix; // for test entities, to prevent unintended interference between tests
	protected LinkedHashSet<ConversationEvent> encEvents;
	protected LinkedHashSet<ConversationEvent> zoomcrEvents;

	@BeforeMethod
	public void prepareTest() throws Exception {
		rabbit.purgeQueue(ITConfiguration.DELETE_QUEUE);
		rabbit.purgeQueue(ITConfiguration.INDEX_QUEUE);
		rabbit.purgeQueue(ITConfiguration.INDEX_QUEUE_DEAD);

		itHelper.deleteConversations();
		dataAccess.resetAll();
		zqmConnector.resetAll();
		scheduler.resetAll();

		start = Instant.now();
		prefix = start.toEpochMilli() + "-";
		reportUrl = new URL("http", "localhost", scheduler.port(), "/api/reports/" + start.toEpochMilli());
		encEvents = new LinkedHashSet<>();
		zoomcrEvents = new LinkedHashSet<>();

		// default admin settings
		setServerTimestamp(start.atZone(ZoneId.of("Africa/Nairobi")));

		// zqm connector allows lookup of events, although returns nothing unless overridden in the test
		zqmConnector.givenThat(post(urlPathEqualTo("/events/lookup"))
				.willReturn(okJson(toJson(new EventList(emptyList())))));

		// zqm connector allows lookup of questionnaires, although returns nothing unless overridden in test
		zqmConnector.givenThat(get(urlPathEqualTo("/questionnaires"))
				.willReturn(okJson(toJson(ImmutableList.of()))));

		// data.service allows lookup of events although returns nothing unless overridden in the test
		dataAccess.givenThat(post(urlPathEqualTo("/v3/events/lookup"))
				.willReturn(okJson(toJson(new EventList(emptyList())))));

		// data.service allows lookup for labels although returns nothing unless overridden in the test
		dataAccess.givenThat(post(urlPathEqualTo("/v3/labels/lookup"))
				.willReturn(okJson(toJson(LabelList.builder().build()))));

		// data.service allows deleting events
		dataAccess.givenThat(post(urlPathEqualTo("/v3/events/delete/lookup"))
				.willReturn(noContent()));

		dataAccess.givenThat(get(urlPathMatching("/v3/search-templates/users/.*"))
				.willReturn(okJson(toJson(new SearchTemplateList(emptyList())))));

		// delete all keycloak users
		keycloakRealm.deleteAllUsersExceptAdmin();
		keycloakRealm.deleteAllRoles();
		keycloakRealm.deleteAllGroups();
	}

	protected String toJson(Object value) {
		try {
			return objectMapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Failed to write as string: " + value, e);
		}
	}

	protected ResponseSpecification badRequest() {
		return expect()
				.statusCode(HTTP_BAD_REQUEST)
				// body is JsonError:
				.body("httpErrorCode", is(HTTP_BAD_REQUEST))
				.body("errorCode", is(UNKNOWN.getCode()))
				.body("errorDescription", notNullValue());
	}

	protected ResponseSpecification protectionFailure() {
		return expect()
				.statusCode(HTTP_CONFLICT)
				// body is JsonError:
				.body("httpErrorCode", is(HTTP_CONFLICT))
				.body("errorCode", is(CONVERSATION_IS_PROTECTED.getCode()))
				.body("errorDescription", is("Conversation is protected by a protection saved search"));
	}

	protected void setServerTimestamp(ZonedDateTime serverTimestamp) {
		dataAccess.givenThat(get(urlPathEqualTo("/v3/administration/settings"))
				.willReturn(okJson(toJson(EncourageSettings.builder()
						.build()))));
	}

	/**
	 * @see #setupIndexedConversations(List)
	 */
	protected void setupIndexedConversations(Conversation... conversations) {
		setupIndexedConversations(asList(conversations));
	}

	/**
	 * Send conversations to indexer, wait for the indexer to process the next batch,
	 * and mock {@code /events/lookup} APIs for each event source to include these events in any/all results.
	 *
	 * @param conversations conversations with events to index, must have conversationIds and eventIds
	 */
	protected void setupIndexedConversations(List<Conversation> conversations) {
		setupSavedEvents(conversations.stream()
				.flatMap(conversation -> conversation.getEvents().stream()));

		// GIVEN conversations are sent for indexing and fully processed
		itHelper.indexConversations(new ConversationList(conversations));
		itHelper.waitForIndexer();

		// forget requests (but not stubs) related to indexing
		zqmConnector.resetRequests();
		dataAccess.resetRequests();
	}

	protected void setupSavedEvents(ConversationEvent... extraEvents) {
		setupSavedEvents(Arrays.stream(extraEvents));
	}

	private void setupSavedEvents(Stream<ConversationEvent> extraEvents) {
		Map<String, List<ConversationEvent>> eventsBySourceSystem = extraEvents
				.collect(groupingBy(event -> getSourceSystem(event.getEventId())));

		// GIVEN zoomcr event lookups now also return callrec events for these conversations
		eventsBySourceSystem.getOrDefault(SOURCE_CALLREC, emptyList()).forEach(event -> {
			zoomcrEvents.remove(event);
			zoomcrEvents.add(event);
		});
		zqmConnector.givenThat(post(urlPathEqualTo("/events/lookup"))
				.willReturn(okJson(toJson(new EventList(new ArrayList<>(zoomcrEvents))))));
		zqmConnector.givenThat(post(urlPathEqualTo("/events")) // update returns events too
				.willReturn(okJson(toJson(new EventList(new ArrayList<>(zoomcrEvents))))));

		// GIVEN data.service event lookups now also return the other events for these conversations
		eventsBySourceSystem.getOrDefault(SOURCE_ENCOURAGE, emptyList()).forEach(event -> {
			encEvents.remove(event);
			encEvents.add(event);
		});
		dataAccess.givenThat(post(urlPathEqualTo("/v3/events/lookup"))
				.willReturn(okJson(toJson(new EventList(new ArrayList<>(encEvents))))));
		dataAccess.givenThat(post(urlPathEqualTo("/v3/events")) // update returns events too
				.willReturn(okJson(toJson(new EventList(new ArrayList<>(encEvents))))));
	}

	protected Condition<EventLookup> lookupForEventIds(String... eventIds) {
		Set<String> eventIdSet = ImmutableSet.copyOf(eventIds);
		return new Condition<>(
				lookup -> lookup != null && ImmutableSet.copyOf(lookup.getEventIds()).equals(eventIdSet),
				"lookup for events with ids %s", eventIdSet);
	}

	protected Condition<LabelLookup> lookupForLabelIds(Long... labelIds) {
		Set<Long> labelIdSet = ImmutableSet.copyOf(labelIds);
		return new Condition<>(
				lookup -> lookup != null && ImmutableSet.copyOf(lookup.getLabelIds()).equals(labelIdSet),
				"lookup for labels with ids %s", labelIdSet);
	}

	protected ConversationResource defaultResource() {
		return ConversationResource.builder().uri(URI.create("callrec:system1")).build();
	}

	protected Role createRole(Activity.Activities activity) {
		return keycloakRealm.createRole(activity.name());
	}
}

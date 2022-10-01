package com.encouragee.camel.clientSearch;

import com.encouragee.camel.clientSearch.config.ITBase;
import com.encouragee.camel.clientSearch.config.ITHelper;
import com.encouragee.camel.clientSearch.model.ConversationsPage;
import com.google.common.collect.Sets;
import com.zoomint.encourage.common.model.label.Label;
import com.zoomint.encourage.common.model.label.LabelList;
import com.zoomint.encourage.common.model.search.SearchTemplate;
import com.zoomint.encourage.common.model.search.SearchTemplateList;
import com.zoomint.encourage.model.conversation.Conversation;
//import com.encouragee.camel.clientSearch.conversation.Conversation;
import com.zoomint.encourage.model.conversation.ConversationEvent;
import com.zoomint.encourage.model.conversation.ConversationParticipant;
import com.zoomint.encourage.model.conversation.ConversationResource;
import com.zoomint.encourage.model.conversation.event.*;
import com.zoomint.encourage.model.search.ClientConversationSearch;
import com.zoomint.encourage.model.search.CommunicationType;
import com.zoomint.encourage.model.search.ReviewState;
import com.zoomint.encourage.model.search.SearchableLabel;
import com.zoomint.keycloak.provider.api.dto.Group;
import com.zoomint.keycloak.provider.api.dto.Role;
import com.zoomint.keycloak.provider.api.dto.User;
import org.assertj.core.util.Lists;
import org.testng.annotations.Test;

import java.net.URI;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.encouragee.camel.clientSearch.model.ConversationDocument.FIELD_RANDOM_PREFIX;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.zoomint.encourage.common.model.user.Activity.Activities.*;
import static com.zoomint.encourage.model.conversation.ConversationParticipantType.AGENT;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Test(description = "Framework Data Service Conversations Search Integration Tests")
public class ConversationsClientSearchIT extends ITBase {
	private static final String PATH_PREFIX = "/";

	public static final String GROUP_A = "Group A";
	public static final String ROOT_GROUP = "Root Group";
	public static final String GROUP_B = "Group B";
	public static final String GROUP_C = "Group C";
	private User userInRoot;
	private User userInGroupA;
	private User userInGroupB;
	private User userInGroupC;
	private User userEvaluateRoot;
	private User userEvaluateGroupA;
	private User userEvaluateGroupB;
	private User userEvaluateGroupC;
	private User userEvaluateGroupBplusC;
	private Map<String, List<String>> viewUserClientRole;

	@Test
	public void findConversationsSortedRandomly() {
		// GIVEN some number of different conversations are indexed
		setupIndexedConversations(IntStream.range(0, 50)
				.mapToObj(index -> Conversation.builder()
						.conversationId("c" + index)
						.event(ChatEvent.builder()
								.eventId("sfdc:chat:" + index)
								.start(ZonedDateTime.parse("2017-01-01T00:00:00Z").plusDays(index))
								.created(Instant.parse("2017-01-01T00:00:00Z").plusSeconds(index))
								.resource(ConversationResource.builder().uri(URI.create("sfdc:chat:" + index)).build())
								.build())
						.build())
				.collect(toList()));

		// WHEN executing different searches with different random sort order
		List<List<String>> randomizedConversationIdLists = IntStream.rangeClosed(-2, 2)
				.mapToObj(index -> itHelper.searchConversations(ITHelper.SearchRequest.builder()
						.sort(FIELD_RANDOM_PREFIX + index)
						.page(0).size(20)
						.clientTimestamp(ZonedDateTime.parse("2018-01-01T00:00:00Z"))
						.search(ClientConversationSearch.builder().build())
						.build()))
				.map(results -> results.getConversations().stream()
						.map(Conversation::getConversationId)
						.collect(toList()))
				.collect(toList());

		// THEN each search returned full page of results, each time in a different order
		assertThat(randomizedConversationIdLists)
				.allSatisfy(list -> assertThat(list).hasSize(20))
				.doesNotHaveDuplicates();
	}

//	@Test
//	public void findConversationViewViewGroupPermissionWithoutGroup()  {
//		// GIVEN some roles in keycloak
//		createRole(INTERACTION_TAGS_ADD);
//		Role compositeRole = keycloakRealm.createRole(Role.builder()
//				.name(INTERACTION_TAGS_EDIT.name())
//				.composites(Set.of(Role.builder()
//						.name(INTERACTIONS_GROUP_VIEW.name())
//						.build()))
//				.build());
//
//		// GIVEN a user with some roles, one of them composite
//		User userWithViewGroupButNoGroup = keycloakRealm.createUser(User.builder()
//				.username("userGroupViewNoGroup")
//				.phoneExtension("420")
//				.role(Role.builder().name(INTERACTION_TAGS_EDIT.name()).build())
//				.role(Role.builder()
//						.name(INTERACTION_TAGS_ADD.name())
//						.composites(Set.of(compositeRole))
//						.build())
//				.build());
//
//		// GIVEN some conversation is indexed without user or group
//		setupIndexedConversations(
//				Conversation.builder()
//						.conversationId(prefix + "conv1")
//						.event(StartedCallEvent.builder()
//								.eventId("callrec:sid:100:started_call")
//								.resource(defaultResource())
//								.start(ZonedDateTime.parse("2014-01-01T16:34:57.102+01:00"))
//								.participant(ConversationParticipant.builder()
//										.type(AGENT)
//										.user(userInRoot)
//										.build())
//								.build())
//						.build());
//
//		// WHEN searching for conversations
//		// THEN Unauthorized
//		given().contentType(JSON).body(ClientConversationSearch.builder()
//				.onBehalfOf(userWithViewGroupButNoGroup)
//				.build())
//				.when().post("/conversations/client-search")
//				.then().statusCode(HTTP_UNAUTHORIZED)
//		;
//	}
//
//
//	@Test
//	public void findConversationOnBehalfOfManager() {
//		// GIVEN some conversation is indexed with user but without group
//		setupIndexedConversations(
//				Conversation.builder()
//						.conversationId(prefix + "conv1")
//						.event(StartedCallEvent.builder()
//								.eventId("callrec:sid:100:started_call")
//								.resource(defaultResource())
//								.start(ZonedDateTime.parse("2014-01-01T16:34:57.102+01:00"))
//								.build())
//						.event(ReviewEvent.builder()
//								.eventId("zoomqm:subevaluationid:id1")
//								.state(ReviewState.PLANNED)
//								.reviewId("zoomqm:subevaluationid:id1")
//								.questionnaireId("zoomqm:questformid:id2")
//								.reviewer(User.builder().userId("UUID-20").build())
//								.reviewee(User.builder().userId("UUID-200").build())
//								.build())
//						.build());
//
//		// WHEN searching for conversations
//		// THEN search results are correct
//		assertThat(
//				itHelper.searchConversations(ClientConversationSearch.builder()
//						.onBehalfOf(User.builder()
//								.userId("UUID-99")
//								.role(Role.builder().name("INTERACTIONS_FULL_VIEW").build())
//								.build())
//						.build())
//						.getConversations())
//				.extracting(Conversation::getConversationId)
//				.containsExactlyInAnyOrder(prefix + "conv1");
//	}
//
//	@Test
//	public void findByGroupPermissions()  {
//		// GIVEN group structure on lookup
//		createRole(INTERACTIONS_GROUP_VIEW);
//		Group groupB = keycloakRealm.createGroup(Group.builder()
//				.groupName(GROUP_B)
//				.externalGroupId("2")
//				.build());
//
//		Group groupA = keycloakRealm.createGroup(Group.builder()
//				.groupName(GROUP_A)
//				.externalGroupId("1")
//				.build(), Lists.newArrayList(groupB));
//
//		Group groupC = keycloakRealm.createGroup(Group.builder()
//				.groupName(GROUP_C)
//				.externalGroupId("3")
//				.build());
//		Group rootGroup = keycloakRealm.createGroup(Group.builder()
//				.groupName(ROOT_GROUP)
//				.externalGroupId("15")
//				.build(), Lists.newArrayList(groupA, groupC));
//
//
//		userInRoot = User.builder()
//				.username("userinroot")
//				.phoneExtension("420")
//				.role(Role.builder().name(INTERACTIONS_GROUP_VIEW.name()).build())
//				.groups(Collections.singleton(groupPath(rootGroup)))
//				.build();
//		userInGroupA =
//				User.builder()
//						.username("useringroupa")
//						.phoneExtension("420")
//						.role(Role.builder().name(INTERACTIONS_GROUP_VIEW.name()).build())
//						.groups(Collections.singleton(groupPath(rootGroup, groupA)))
//						.build();
//		userInGroupB = User.builder()
//				.username("useringroupb")
//				.phoneExtension("420")
//				.role(Role.builder().name(INTERACTIONS_GROUP_VIEW.name()).build())
//				.groups(Collections.singleton(groupPath(rootGroup, groupA, groupB)))
//				.build();
//		userInGroupC = User.builder()
//				.username("useringroupc")
//				.phoneExtension("420")
//				.role(Role.builder().name(INTERACTIONS_GROUP_VIEW.name()).build())
//				.groups(Collections.singleton(groupPath(rootGroup, groupC)))
//				.build();
//
//		keycloakRealm.createUser(userInRoot);
//		keycloakRealm.createUser(userInGroupA);
//		keycloakRealm.createUser(userInGroupB);
//		keycloakRealm.createUser(userInGroupC);
//
//		userEvaluateRoot = User.builder()
//				.username("userEvaluateRoot")
//				.role(Role.builder().name(INTERACTIONS_GROUP_VIEW.name()).build())
//				.phoneExtension("420")
//				.usersTeams(Collections.singleton(rootGroup.getGroupId()))
//				.build();
//		userEvaluateGroupA = User.builder()
//				.username("userEvaluateGroupA")
//				.phoneExtension("420")
//				.role(Role.builder().name(INTERACTIONS_GROUP_VIEW.name()).build())
//				.usersTeams(Collections.singleton(groupA.getGroupId()))
//				.build();
//		userEvaluateGroupB = User.builder()
//				.username("userEvaluateGroupB")
//				.phoneExtension("420")
//				.role(Role.builder().name(INTERACTIONS_GROUP_VIEW.name()).build())
//				.usersTeams(Collections.singleton(groupB.getGroupId()))
//				.build();
//		userEvaluateGroupC = User.builder()
//				.username("userEvaluateGroupC")
//				.phoneExtension("420")
//				.role(Role.builder().name(INTERACTIONS_GROUP_VIEW.name()).build())
//				.usersTeams(Collections.singleton(groupC.getGroupId()))
//				.build();
//		userEvaluateGroupBplusC = User.builder()
//				.username("userEvaluateGroupBplusC")
//				.phoneExtension("420")
//				.role(Role.builder().name(INTERACTIONS_GROUP_VIEW.name()).build())
//				.usersTeams(Sets.newHashSet(groupB.getGroupId(), groupC.getGroupId()))
//				.build();
//		keycloakRealm.createUser(userEvaluateRoot);
//		keycloakRealm.createUser(userEvaluateGroupA);
//		keycloakRealm.createUser(userEvaluateGroupB);
//		keycloakRealm.createUser(userEvaluateGroupC);
//		keycloakRealm.createUser(userEvaluateGroupBplusC);
//
//		// GIVEN conversations with various agents (belonging to the groups above)
//		setupIndexedConversations(
//				Conversation.builder()
//						.conversationId(prefix + "conv1")
//						.event(StartedCallEvent.builder()
//								.eventId("callrec:sid:one:started")
//								.resource(defaultResource())
//								.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//								.participant(ConversationParticipant.builder()
//										.type(AGENT)
//										.user(userInRoot)
//										.build())
//								.build())
//						.event(JoinedCallEvent.builder()
//								.eventId("callrec:sid:one:joined")
//								.resource(defaultResource())
//								.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//								.participant(ConversationParticipant.builder()
//										.type(AGENT)
//										.user(userInGroupB)
//										.build())
//								.build())
//						.build(),
//				Conversation.builder()
//						.conversationId(prefix + "conv2")
//						.event(StartedCallEvent.builder()
//								.eventId("callrec:sid:two:started")
//								.resource(defaultResource())
//								.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//								.participant(ConversationParticipant.builder()
//										.type(AGENT)
//										.user(userInGroupA)
//										.build())
//								.build())
//						.build(),
//				Conversation.builder()
//						.conversationId(prefix + "conv3")
//						.event(StartedCallEvent.builder()
//								.eventId("callrec:sid:three:started")
//								.resource(defaultResource())
//								.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//								.participant(ConversationParticipant.builder()
//										.type(AGENT)
//										.user(userInGroupB)
//										.build())
//								.build())
//						.build(),
//				Conversation.builder()
//						.conversationId(prefix + "conv4")
//						.event(StartedCallEvent.builder()
//								.eventId("callrec:sid:four:started")
//								.resource(defaultResource())
//								.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//								.participant(ConversationParticipant.builder()
//										.type(AGENT)
//										.user(userInGroupC)
//										.build())
//								.build())
//						.build(),
//				Conversation.builder()
//						.conversationId(prefix + "conv5")
//						.event(StartedCallEvent.builder()
//								.eventId("callrec:sid:five:started")
//								.resource(defaultResource())
//								.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//								.participant(ConversationParticipant.builder()
//										.type(AGENT)
//										.user(userInRoot)
//										.build())
//								.build())
//						.build());
//
//
//		// WHEN searching on behalf of a supervisor of a specific group
//		// THEN search results are correct
//		assertSoftly(softly -> {
//			softly.assertThat(
//					itHelper.searchConversations(
//							ClientConversationSearch.builder()
//									.onBehalfOf(userEvaluateRoot)
//									.build())
//							.getConversations())
//					.extracting(Conversation::getConversationId)
//					.containsExactlyInAnyOrder(prefix + "conv1", prefix + "conv2", prefix + "conv3", prefix + "conv4", prefix + "conv5");
//
//			softly.assertThat(
//					itHelper.searchConversations(
//							ClientConversationSearch.builder()
//									.onBehalfOf(userEvaluateGroupA)
//									.build())
//							.getConversations())
//					.extracting(Conversation::getConversationId)
//					.containsExactlyInAnyOrder(prefix + "conv1", prefix + "conv2", prefix + "conv3");
//
//			softly.assertThat(
//					itHelper.searchConversations(
//							ClientConversationSearch.builder()
//									.onBehalfOf(userEvaluateGroupB)
//									.build())
//							.getConversations())
//					.extracting(Conversation::getConversationId)
//					.containsExactlyInAnyOrder(prefix + "conv1", prefix + "conv3");
//
//			softly.assertThat(
//					itHelper.searchConversations(
//							ClientConversationSearch.builder()
//									.onBehalfOf(userEvaluateGroupC)
//									.build())
//							.getConversations())
//					.extracting(Conversation::getConversationId)
//					.containsExactlyInAnyOrder(prefix + "conv4");
//
//			softly.assertThat(
//					itHelper.searchConversations(
//							ClientConversationSearch.builder()
//									.onBehalfOf(userEvaluateGroupBplusC)
//									.build())
//							.getConversations())
//					.extracting(Conversation::getConversationId)
//					.containsExactlyInAnyOrder(prefix + "conv1", prefix + "conv3", prefix + "conv4");
//		});
//	}
//
//
//	@Test
//	public void findByCursor() {
//		// GIVEN some groups in keycloak
//		final Group group1 =
//				keycloakRealm.createGroup(Group.builder()
//						.groupId("gid01")
//						.groupName("group1")
//						.externalGroupId("1")
//						.build());
//		final Group group2 =
//				keycloakRealm.createGroup(Group.builder()
//						.groupId("gid02")
//						.groupName("group2")
//						.externalGroupId("2").build());
//
//		// GIVEN some role in keycloak
//		keycloakRealm.createRole(INTERACTIONS_GROUP_VIEW.name());
//
//		// GIVEN an agent and a supervisor in keycloak
//		User agent = keycloakRealm.createUser(User.builder()
//				.username("fhierro")
//				.groups(Set.of(groupPath(group1)))
//				.phoneExtension("420")
//				.build());
//		User supervisor = keycloakRealm.createUser(User.builder()
//				.username("fglopez")
//				.groups(Set.of(groupPath(group2)))
//				.phoneExtension("420")
//				// should not see conversations without participant
//				.role(Role.builder().name(INTERACTIONS_GROUP_VIEW.name()).build())
//				.build());
//
//		// GIVEN conversations with various agents (belonging to the groups above)
//		ConversationParticipant participant = ConversationParticipant.builder()
//				.type(AGENT)
//				.user(User.builder().userId(agent.getUserId()).build())
//				.build();
//
//		setupIndexedConversations(
//				Conversation.builder()
//						.conversationId(prefix + "conv1")
//						.event(StartedCallEvent.builder()
//								.eventId("global:callrec:sid:one:started")
//								.resource(defaultResource())
//								.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//								.participant(participant)
//								.build())
//						.event(JoinedCallEvent.builder()
//								.eventId("global:callrec:sid:one:joined")
//								.resource(defaultResource())
//								.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//								.participant(participant)
//								.build())
//						.build(),
//				Conversation.builder()
//						.conversationId(prefix + "conv2") //conv2 does not have participant, should be skipped
//						.event(StartedCallEvent.builder()
//								.eventId("global:callrec:sid:two:started")
//								.resource(defaultResource())
//								.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//								.build())
//						.build(),
//				Conversation.builder()
//						.conversationId(prefix + "conv3")
//						.event(StartedCallEvent.builder()
//								.eventId("global:callrec:sid:three:started")
//								.resource(defaultResource())
//								.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//								.participant(participant)
//								.build())
//						.build(),
//				Conversation.builder()
//						.conversationId(prefix + "conv4")
//						.event(StartedCallEvent.builder()
//								.eventId("global:callrec:sid:four:started")
//								.resource(defaultResource())
//								.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//								.participant(participant)
//								.build())
//						.build(),
//				Conversation.builder()
//						.conversationId(prefix + "conv5")
//						.event(StartedCallEvent.builder()
//								.eventId("global:callrec:sid:five:started")
//								.resource(defaultResource())
//								.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//								.participant(participant)
//								.build())
//						.build());
//
//		// WHEN searching on behalf of a supervisor of a specific group AND using
//		// THEN search results are correct
//		assertSoftly(softly -> {
//			ITHelper.SearchRequest searchRequest = ITHelper.SearchRequest.builder()
//					.search(ClientConversationSearch.builder()
//							.onBehalfOf(User.builder()
//									.userId(supervisor.getUserId())
//									.role(Role.builder().name(INTERACTIONS_GROUP_VIEW.name()).build())
//									.usersTeams(Set.of(group1.getGroupId()))
//									.build())
//							.build())
//					.sort("latestSegmentStartDateTime,asc")
//					.sort("conversationId,asc")
//					.cursorMark("*")
//					.size(1)
//					.build();
//			ConversationsPage conversationsPage = itHelper.searchConversations(searchRequest);
//			softly.assertThat(
//					conversationsPage
//							.getConversations())
//					.extracting(Conversation::getConversationId)
//					.containsExactlyInAnyOrder(prefix + "conv1");
//
//			//use cursor, get next conversations
//			conversationsPage = itHelper.searchConversations(searchRequest.toBuilder()
//					.cursorMark(conversationsPage.getPage().getCursorMark())
//					.build());
//			softly.assertThat(
//					conversationsPage
//							.getConversations())
//					.extracting(Conversation::getConversationId)
//					.containsExactlyInAnyOrder(prefix + "conv3");
//
//			//use cursor, get different
//			conversationsPage = itHelper.searchConversations(searchRequest.toBuilder()
//					.cursorMark(conversationsPage.getPage().getCursorMark())
//					.build());
//			softly.assertThat(
//					conversationsPage
//							.getConversations())
//					.extracting(Conversation::getConversationId)
//					.containsExactlyInAnyOrder(prefix + "conv4");
//		});
//	}
//
//
//	@Test
//	public void cursorSearchFailsWithoutUniqueSort() {
//		// WHEN searching using cursorMark without sort by unique key
//		ITHelper.SearchRequest request = ITHelper.SearchRequest.builder().search(ClientConversationSearch.builder().build())
//				.sort("latestSegmentStartDateTime,asc")
//				.cursorMark("*")
//				.build();
//		//THEN bad request is the response
//		given().contentType(JSON).body(request.getSearch())
//				.queryParams(request.getParams())
//				.when().post("/conversations/client-search")
//				.then().assertThat().statusCode(HTTP_BAD_REQUEST);
//	}
//
//	@Test
//	public void cursorSearchFailsWithPageSet() {
//		// WHEN searching using cursorMark without sort by unique key
//		ITHelper.SearchRequest request = ITHelper.SearchRequest.builder().search(ClientConversationSearch.builder().build())
//				.sort("conversationId,asc")
//				.page(2)
//				.cursorMark("*")
//				.build();
//		//THEN bad request is the response
//		given().contentType(JSON).body(request.getSearch())
//				.queryParams(request.getParams())
//				.when().post("/conversations/client-search")
//				.then().assertThat().statusCode(HTTP_BAD_REQUEST);
//	}
//
//
//	@Test
//	public void tagEventShouldBeFilteredOut() {
//
//		// GIVEN a conversation to be indexed
//		Conversation conversation = Conversation.builder()
//				.conversationId(prefix + "conv1")
//				.event(StartedCallEvent.builder()
//						.eventId("callrec:sid:one:started")
//						.resource(defaultResource())
//						.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//						.participant(ConversationParticipant.builder()
//								.type(AGENT)
//								.user(User.builder().userId("UUID-11").build())
//								.build())
//						.build())
//				.event(TagEvent.builder()
//						.eventId("callrec:sid:one:tag")
//						.resource(defaultResource())
//						.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//						.label(Label.builder().labelId(1L).build())
//						.participant(ConversationParticipant.builder()
//								.type(AGENT)
//								.user(User.builder().userId("UUID-11").build())
//								.build())
//						.build())
//				.build();
//		setupIndexedConversations(conversation);
//
//		// WHEN searching for a conversation with limited activities
//		ConversationsPage resultPage = itHelper.searchConversations(ClientConversationSearch.builder()
//				.onBehalfOf(User.builder()
//						.userId("UUID-11")
//						.role(Role.builder().name(INTERACTIONS_AGENT_VIEW.name()).build())
//						.build())
//				.build());
//
//		// THEN the resulting conversation only has the events the user is able to see
//		assertThat(resultPage.getConversations())
//				.flatExtracting(Conversation::getEvents)
//				.extracting(ConversationEvent::getType)
//				.contains(ConversationEvent.STARTED_CALL)
//				.doesNotContain(ConversationEvent.TAG);
//
//		// AND data access is not queried for details about the tag (label)
//		dataAccess.verify(0, postRequestedFor(urlPathEqualTo("/v3/labels/lookup"))
//				.withQueryParam("projection", equalTo("full")));
//	}
//
//	@Test
//	public void shouldSeeOwnEventEvenIfNoOtherPermissions() {
//
//		// GIVEN a conversation to be indexed
//		Conversation conversation = Conversation.builder()
//				.conversationId(prefix + "conv1")
//				.event(StartedCallEvent.builder()
//						.eventId("callrec:sid:one:started")
//						.resource(defaultResource())
//						.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//						.participant(ConversationParticipant.builder()
//								.type(AGENT)
//								.user(User.builder().userId("UUID-11").build())
//								.build())
//						.build())
//				.event(TagEvent.builder()
//						.eventId("callrec:sid:one:tag")
//						.createdBy(User.builder().userId("UUID-11").build())
//						.resource(defaultResource())
//						.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//						.label(Label.builder().labelId(1L).build())
//						.participant(ConversationParticipant.builder()
//								.type(AGENT)
//								.user(User.builder().userId("UUID-11").build())
//								.build())
//						.build())
//				.build();
//		setupIndexedConversations(conversation);
//
//		// GIVEN label lookup returns even more labels than the one used in the conversation
//		dataAccess.givenThat(post(urlPathEqualTo("/v3/labels/lookup"))
//				.willReturn(okJson(toJson(LabelList.builder()
//						.label(Label.builder().labelId(1L).name("label1").build())
//						.label(Label.builder().labelId(2L).name("label2").build())
//						.build()))));
//
//		// WHEN searching for a conversation with limited activities
//		ConversationsPage resultPage = itHelper.searchConversations(ClientConversationSearch.builder()
//				.onBehalfOf(User.builder()
//						.userId("UUID-11")
//						.role(Role.builder().name(INTERACTIONS_AGENT_VIEW.name()).build())
//						.build())
//				.build());
//
//		// THEN the resulting conversation only has the events the user is able to see
//		assertThat(resultPage.getConversations())
//				.flatExtracting(Conversation::getEvents)
//				.extracting(ConversationEvent::getType)
//				.containsExactlyInAnyOrder(ConversationEvent.STARTED_CALL, ConversationEvent.TAG);
//	}
//
//	@Test
//	public void failIfNoViewPermissions() {
//		// WHEN searching on behalf of a user with no relevant permissions
//		given()
//				.contentType(JSON)
//				.body(ClientConversationSearch.builder()
//						.onBehalfOf(User.builder()
//								.userId("UUID-99")
//								.role(Role.builder().name(INTERACTION_TAGS_EDIT.name()).build())
//								.build())
//						.build())
//				.when().post("/conversations/client-search")
//
//				// THEN operation fails with 401
//				.then().statusCode(HTTP_UNAUTHORIZED);
//	}
//
//	@Test
//	public void findById() {
//		// GIVEN a conversation to be indexed
//		Conversation conversation = Conversation.builder()
//				.conversationId(prefix + "conv101")
//				.event(StartedCallEvent.builder()
//						.eventId("callrec:sid:one:started")
//						.resource(defaultResource())
//						.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//						.participant(ConversationParticipant.builder()
//								.type(AGENT)
//								.user(User.builder().userId("UUID-11").build())
//								.build())
//						.build())
//				.build();
//		setupIndexedConversations(conversation);
//
//		// WHEN searching for a conversation only by id
//		ConversationsPage resultPage = itHelper.searchConversations(ClientConversationSearch.builder()
//				.conversationId(prefix + "conv101")
//				.build());
//
//		assertThat(resultPage.getConversations())
//				.extracting(Conversation::getConversationId)
//				.containsExactly(prefix + "conv101");
//	}
//
//	@Test
//	public void shouldSeeOwnAndConversationFromAssignedFilter() {
//
//		// GIVEN a conversations to be indexed
//		Conversation conversation = Conversation.builder()
//				.conversationId(prefix + "conv1")
//				.event(StartedCallEvent.builder()
//						.eventId("callrec:sid:one:started")
//						.resource(defaultResource())
//						.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//						.participant(ConversationParticipant.builder()
//								.type(AGENT)
//								.user(User.builder().userId("userIdWithAssignedFilters").build())
//								.build())
//						.build())
//				.event(TagEvent.builder()
//						.eventId("callrec:sid:one:tag")
//						.createdBy(User.builder().userId("userIdWithAssignedFilters").build())
//						.resource(defaultResource())
//						.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//						.label(Label.builder().labelId(1L).build())
//						.participant(ConversationParticipant.builder()
//								.type(AGENT)
//								.user(User.builder().userId("userIdWithAssignedFilters").build())
//								.build())
//						.build())
//				.build();
//		Conversation conversationNoParticipant = Conversation.builder()
//				.conversationId("conversationIdFromAssignedFilter")
//				.event(EmailEvent.builder()
//						.eventId("callrec:sid:one:email")
//						.resource(defaultResource())
//						.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//						.participant(ConversationParticipant.builder()
//								.type(AGENT)
//								.user(User.builder().userId("UUID-12").build())
//								.build())
//						.build())
//				.build();
//		setupIndexedConversations(conversation, conversationNoParticipant);
//
//		// GIVEN label lookup returns even more labels than the one used in the conversation
//		dataAccess.givenThat(post(urlPathEqualTo("/v3/labels/lookup"))
//				.willReturn(okJson(toJson(LabelList.builder()
//						.label(Label.builder().labelId(1L).name("label1").build())
//						.label(Label.builder().labelId(2L).name("label2").build())
//						.build()))));
//		// GIVEN encourage-data returns single user assigned filter with EMAIL communication type
//		dataAccess.givenThat(get(urlMatching("/v3/search-templates/users/.*"))
//				.willReturn(okJson(toJson(new SearchTemplateList(List.of(SearchTemplate.builder().conversationSearch(
//						ClientConversationSearch.builder().communicationTypeIncludes(List.of(CommunicationType.EMAIL)).build()).build()))))));
//
//		// WHEN searching for a conversation with limited activities
//		ConversationsPage resultPage = itHelper.searchConversations(ClientConversationSearch.builder()
//				.onBehalfOf(User.builder()
//						.userId("userIdWithAssignedFilters")
//						.role(Role.builder().name(INTERACTIONS_AGENT_VIEW.name()).build())
//						.build())
//				.build());
//
//		// THEN the resulting conversation only has the events the user is able to see plus the one from the ABAC saved filter
//		assertThat(resultPage.getConversations()).asList().hasSize(2);
//	}
//
//	@Test
//	public void shouldSeeOwnAndConversationsFromAssignedFilters() {
//
//		// GIVEN a conversations to be indexed
//		Conversation conversation = Conversation.builder()
//				.conversationId(prefix + "conv1")
//				.event(StartedCallEvent.builder()
//						.eventId("callrec:sid:one:started")
//						.resource(defaultResource())
//						.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//						.participant(ConversationParticipant.builder()
//								.type(AGENT)
//								.user(User.builder().userId("userIdWithAssignedFilters").build())
//								.build())
//						.build())
//				.event(TagEvent.builder()
//						.eventId("callrec:sid:one:tag")
//						.createdBy(User.builder().userId("userIdWithAssignedFilters").build())
//						.resource(defaultResource())
//						.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//						.label(Label.builder().labelId(1L).build())
//						.participant(ConversationParticipant.builder()
//								.type(AGENT)
//								.user(User.builder().userId("userIdWithAssignedFilters").build())
//								.build())
//						.build())
//				.build();
//		Conversation conversationEmail = Conversation.builder()
//				.conversationId("conversationIdFromAssignedFilter1")
//				.event(EmailEvent.builder()
//						.eventId("callrec:sid:one:email")
//						.resource(defaultResource())
//						.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//						.participant(ConversationParticipant.builder()
//								.type(AGENT)
//								.user(User.builder().userId("UUID-12").build())
//								.build())
//						.build())
//				.build();
//		Conversation conversationChat = Conversation.builder()
//				.conversationId("conversationIdFromAssignedFilter2")
//				.event(ChatEvent.builder()
//						.eventId("callrec:sid:one:chat")
//						.resource(defaultResource())
//						.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//						.participant(ConversationParticipant.builder()
//								.type(AGENT)
//								.user(User.builder().userId("UUID-12").build())
//								.build())
//						.build())
//				.build();
//		setupIndexedConversations(conversation, conversationEmail, conversationChat);
//
//		// GIVEN label lookup returns even more labels than the one used in the conversation
//		dataAccess.givenThat(post(urlPathEqualTo("/v3/labels/lookup"))
//				.willReturn(okJson(toJson(LabelList.builder()
//						.label(Label.builder().labelId(1L).name("label1").build())
//						.label(Label.builder().labelId(2L).name("label2").build())
//						.build()))));
//		// GIVEN encourage-data returns two user assigned filters with EMAIL and CHAT communication types
//		dataAccess.givenThat(get(urlMatching("/v3/search-templates/users/.*"))
//				.willReturn(okJson(toJson(new SearchTemplateList(List.of(
//						SearchTemplate.builder().conversationSearch(
//						ClientConversationSearch.builder().communicationTypeIncludes(List.of(CommunicationType.EMAIL)).build()).build(),
//						SearchTemplate.builder().conversationSearch(
//						ClientConversationSearch.builder().communicationTypeIncludes(List.of(CommunicationType.CHAT)).build()).build()))))));
//
//		// WHEN searching for a conversation with limited activities
//		ConversationsPage resultPage = itHelper.searchConversations(ClientConversationSearch.builder()
//				.onBehalfOf(User.builder()
//						.userId("userIdWithAssignedFilters")
//						.role(Role.builder().name(INTERACTIONS_AGENT_VIEW.name()).build())
//						.build())
//				.build());
//
//		// THEN the resulting conversation only has the events the user is able to see plus the two from the ABAC saved filters
//		assertThat(resultPage.getConversations()).asList().hasSize(3);
//	}
//
//	@Test
//	public void shouldFilterOutTheConversationsByTheUIFilter() {
//
//		// GIVEN a conversations to be indexed
//		Conversation conversation = Conversation.builder()
//				.conversationId(prefix + "conv1")
//				.event(StartedCallEvent.builder()
//						.eventId("callrec:sid:one:started")
//						.resource(defaultResource())
//						.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//						.participant(ConversationParticipant.builder()
//								.type(AGENT)
//								.user(User.builder().userId("userIdWithAssignedFilters").build())
//								.build())
//						.build())
//				.event(TagEvent.builder()
//						.eventId("callrec:sid:one:tag")
//						.createdBy(User.builder().userId("userIdWithAssignedFilters").build())
//						.resource(defaultResource())
//						.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//						.label(Label.builder().labelId(1L).build())
//						.participant(ConversationParticipant.builder()
//								.type(AGENT)
//								.user(User.builder().userId("userIdWithAssignedFilters").build())
//								.build())
//						.build())
//				.build();
//		Conversation conversationEmail = Conversation.builder()
//				.conversationId("conversationIdFromAssignedFilter1")
//				.event(EmailEvent.builder()
//						.eventId("callrec:sid:one:email")
//						.resource(defaultResource())
//						.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//						.participant(ConversationParticipant.builder()
//								.type(AGENT)
//								.user(User.builder().userId("UUID-12").build())
//								.build())
//						.build())
//				.build();
//		setupIndexedConversations(conversation, conversationEmail);
//
//		// GIVEN label lookup returns even more labels than the one used in the conversation
//		dataAccess.givenThat(post(urlPathEqualTo("/v3/labels/lookup"))
//				.willReturn(okJson(toJson(LabelList.builder()
//						.label(Label.builder().labelId(1L).name("label1").build())
//						.label(Label.builder().labelId(2L).name("label2").build())
//						.build()))));
//		// GIVEN encourage-data returns two user assigned filters with EMAIL and CHAT communication types
//		dataAccess.givenThat(get(urlMatching("/v3/search-templates/users/.*"))
//				.willReturn(okJson(toJson(new SearchTemplateList(List.of(
//						SearchTemplate.builder().conversationSearch(
//						ClientConversationSearch.builder().communicationTypeIncludes(List.of(CommunicationType.EMAIL)).build()).build(),
//						SearchTemplate.builder().conversationSearch(
//						ClientConversationSearch.builder().communicationTypeIncludes(List.of(CommunicationType.CHAT)).build()).build()))))));
//
//		// WHEN searching for a conversation with limited activities
//		ConversationsPage resultPage = itHelper.searchConversations(ClientConversationSearch.builder()
//				.communicationTypeIncludes(List.of(CommunicationType.CHAT))
//				.onBehalfOf(User.builder()
//						.userId("userIdWithAssignedFilters")
//						.role(Role.builder().name(INTERACTIONS_AGENT_VIEW.name()).build())
//						.build())
//				.build());
//
//		// THEN the resulting conversation list is empty as there is no conversation of type CHAT in the SOLR
//		assertThat(resultPage.getConversations()).asList().isEmpty();
//	}
//
//
//	@Test
//	public void shouldSeeOwnAndConversationsFromAssignedFiltersFullText() {
//
//		// GIVEN a conversations to be indexed
//		Conversation conversation = Conversation.builder()
//				.conversationId(prefix + "conv1")
//				.event(StartedCallEvent.builder()
//						.eventId("callrec:sid:one:started")
//						.resource(defaultResource())
//						.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//						.participant(ConversationParticipant.builder()
//								.type(AGENT)
//								.user(User.builder().userId("userIdWithAssignedFilters").build())
//								.build())
//						.build())
//				.event(TagEvent.builder()
//						.eventId("callrec:sid:one:tag")
//						.createdBy(User.builder().userId("userIdWithAssignedFilters").build())
//						.resource(defaultResource())
//						.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//						.label(Label.builder().labelId(1L).build())
//						.participant(ConversationParticipant.builder()
//								.type(AGENT)
//								.user(User.builder().userId("userIdWithAssignedFilters").build())
//								.build())
//						.build())
//				.build();
//		Conversation conversationEmail = Conversation.builder()
//				.conversationId("conversationIdFromAssignedFilter1")
//				.event(EmailEvent.builder()
//						.eventId("callrec:sid:one:email")
//						.resource(defaultResource())
//						.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//						.participant(ConversationParticipant.builder()
//								.type(AGENT)
//								.user(User.builder().userId("UUID-12").build())
//								.build())
//						.build())
//				.build();
//		Conversation conversationChat = Conversation.builder()
//				.conversationId("conversationIdFromAssignedFilter2")
//				.event(ChatEvent.builder()
//						.eventId("callrec:sid:one:chat")
//						.resource(defaultResource())
//						.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//						.participant(ConversationParticipant.builder()
//								.type(AGENT)
//								.user(User.builder().userId("UUID-12").build())
//								.build())
//						.build())
//				.build();
//		setupIndexedConversations(conversation, conversationEmail, conversationChat);
//
//		// GIVEN label lookup returns even more labels than the one used in the conversation
//		dataAccess.givenThat(post(urlPathEqualTo("/v3/labels/lookup"))
//				.willReturn(okJson(toJson(LabelList.builder()
//						.label(Label.builder().labelId(1L).name("label1").build())
//						.label(Label.builder().labelId(2L).name("label2").build())
//						.build()))));
//		// GIVEN encourage-data returns two user assigned filters with EMAIL and CHAT communication types
//		dataAccess.givenThat(get(urlMatching("/v3/search-templates/users/.*"))
//				.willReturn(okJson(toJson(new SearchTemplateList(List.of(
//						SearchTemplate.builder().conversationSearch(
//								ClientConversationSearch.builder().fulltext("conversationIdFromAssignedFilter1").build()).build()))))));
//
//		// WHEN searching for a conversation with limited activities
//		ConversationsPage resultPage = itHelper.searchConversations(ClientConversationSearch.builder()
//				.onBehalfOf(User.builder()
//						.userId("userIdWithAssignedFilters")
//						.role(Role.builder().name(INTERACTIONS_AGENT_VIEW.name()).build())
//						.build())
//				.build());
//
//		// THEN the resulting conversation only has the events the user is able to see plus the two from the ABAC saved filters
//		assertThat(resultPage.getConversations()).asList().hasSize(2);
//	}
//
//	@Test
//	public void shouldEnrichSavedFiltersWithSubgroups() {
//		// GIVEN group structure on lookup
//		createRole(INTERACTIONS_GROUP_VIEW);
//		Group groupB = keycloakRealm.createGroup(Group.builder()
//				.groupName(GROUP_B)
//				.externalGroupId("2")
//				.build());
//
//		Group groupA = keycloakRealm.createGroup(Group.builder()
//				.groupName(GROUP_A)
//				.externalGroupId("1")
//				.build(), Lists.newArrayList(groupB));
//
//		Group groupC = keycloakRealm.createGroup(Group.builder()
//				.groupName(GROUP_C)
//				.externalGroupId("3")
//				.build());
//		Group rootGroup = keycloakRealm.createGroup(Group.builder()
//				.groupName(ROOT_GROUP)
//				.externalGroupId("15")
//				.build(), Lists.newArrayList(groupA, groupC));
//
//
//		userInRoot = User.builder()
//				.username("userinroot")
//				.phoneExtension("420")
//				.role(Role.builder().name(INTERACTIONS_GROUP_VIEW.name()).build())
//				.groups(Collections.singleton(groupPath(rootGroup)))
//				.build();
//		userInGroupA =
//				User.builder()
//						.username("useringroupa")
//						.phoneExtension("420")
//						.role(Role.builder().name(INTERACTIONS_GROUP_VIEW.name()).build())
//						.groups(Collections.singleton(groupPath(rootGroup, groupA)))
//						.build();
//		userInGroupB = User.builder()
//				.username("useringroupb")
//				.phoneExtension("420")
//				.role(Role.builder().name(INTERACTIONS_GROUP_VIEW.name()).build())
//				.groups(Collections.singleton(groupPath(rootGroup, groupA, groupB)))
//				.build();
//		userInGroupC = User.builder()
//				.userId("userIdWithAssignedFilters")
//				.username("useringroupc")
//				.phoneExtension("420")
//				.role(Role.builder().name(INTERACTIONS_AGENT_VIEW.name()).build())
//				.groups(Collections.singleton(groupPath(rootGroup, groupC)))
//				.build();
//
//		keycloakRealm.createUser(userInRoot);
//		keycloakRealm.createUser(userInGroupA);
//		keycloakRealm.createUser(userInGroupB);
//		keycloakRealm.createUser(userInGroupC);
//
//		userEvaluateRoot = User.builder()
//				.username("userEvaluateRoot")
//				.role(Role.builder().name(INTERACTIONS_GROUP_VIEW.name()).build())
//				.phoneExtension("420")
//				.usersTeams(Collections.singleton(rootGroup.getGroupId()))
//				.build();
//		userEvaluateGroupA = User.builder()
//				.username("userEvaluateGroupA")
//				.phoneExtension("420")
//				.role(Role.builder().name(INTERACTIONS_GROUP_VIEW.name()).build())
//				.usersTeams(Collections.singleton(groupA.getGroupId()))
//				.build();
//		userEvaluateGroupB = User.builder()
//				.username("userEvaluateGroupB")
//				.phoneExtension("420")
//				.role(Role.builder().name(INTERACTIONS_GROUP_VIEW.name()).build())
//				.usersTeams(Collections.singleton(groupB.getGroupId()))
//				.build();
//		userEvaluateGroupC = User.builder()
//				.username("userEvaluateGroupC")
//				.phoneExtension("420")
//				.role(Role.builder().name(INTERACTIONS_GROUP_VIEW.name()).build())
//				.usersTeams(Collections.singleton(groupC.getGroupId()))
//				.build();
//		userEvaluateGroupBplusC = User.builder()
//				.username("userEvaluateGroupBplusC")
//				.phoneExtension("420")
//				.role(Role.builder().name(INTERACTIONS_GROUP_VIEW.name()).build())
//				.usersTeams(Sets.newHashSet(groupB.getGroupId(), groupC.getGroupId()))
//				.build();
//		keycloakRealm.createUser(userEvaluateRoot);
//		keycloakRealm.createUser(userEvaluateGroupA);
//		keycloakRealm.createUser(userEvaluateGroupB);
//		keycloakRealm.createUser(userEvaluateGroupC);
//		keycloakRealm.createUser(userEvaluateGroupBplusC);
//
//		// GIVEN conversations with various agents (belonging to the groups above)
//		setupIndexedConversations(
//				Conversation.builder()
//						.conversationId(prefix + "conv1")
//						.event(StartedCallEvent.builder()
//								.eventId("callrec:sid:one:started")
//								.resource(defaultResource())
//								.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//								.participant(ConversationParticipant.builder()
//										.type(AGENT)
//										.user(userInRoot)
//										.build())
//								.build())
//						.event(JoinedCallEvent.builder()
//								.eventId("callrec:sid:one:joined")
//								.resource(defaultResource())
//								.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//								.participant(ConversationParticipant.builder()
//										.type(AGENT)
//										.user(userInGroupB)
//										.build())
//								.build())
//						.build(),
//				Conversation.builder()
//						.conversationId(prefix + "conv2")
//						.event(StartedCallEvent.builder()
//								.eventId("callrec:sid:two:started")
//								.resource(defaultResource())
//								.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//								.participant(ConversationParticipant.builder()
//										.type(AGENT)
//										.user(userInGroupA)
//										.build())
//								.build())
//						.build(),
//				Conversation.builder()
//						.conversationId(prefix + "conv3")
//						.event(StartedCallEvent.builder()
//								.eventId("callrec:sid:three:started")
//								.resource(defaultResource())
//								.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//								.participant(ConversationParticipant.builder()
//										.type(AGENT)
//										.user(userInGroupB)
//										.build())
//								.build())
//						.build(),
//				Conversation.builder()
//						.conversationId(prefix + "conv4")
//						.event(StartedCallEvent.builder()
//								.eventId("callrec:sid:four:started")
//								.resource(defaultResource())
//								.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//								.participant(ConversationParticipant.builder()
//										.type(AGENT)
//										.user(userInGroupC)
//										.build())
//								.build())
//						.build(),
//				Conversation.builder()
//						.conversationId(prefix + "conv5")
//						.event(StartedCallEvent.builder()
//								.eventId("callrec:sid:five:started")
//								.resource(defaultResource())
//								.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//								.participant(ConversationParticipant.builder()
//										.type(AGENT)
//										.user(userInRoot)
//										.build())
//								.build())
//						.build());
//
//		dataAccess.givenThat(get(urlMatching("/v3/search-templates/users/.*"))
//				.willReturn(okJson(toJson(new SearchTemplateList(List.of(
//						SearchTemplate.builder().conversationSearch(
//								ClientConversationSearch.builder().groupUUID(rootGroup.getGroupId()).build()).build()))))));
//
//		// WHEN searching on behalf of a agent from group C
//		// THEN search results are correct
//		assertSoftly(softly -> {
//			softly.assertThat(
//							itHelper.searchConversations(
//											ClientConversationSearch.builder()
//													.onBehalfOf(userInGroupC)
//													.build())
//									.getConversations())
//					.extracting(Conversation::getConversationId)
//					.hasSize(5);
//		});
//	}
//
//	@Test
//	public void shouldEnrichSavedFiltersWithLabels() {
//
//		// GIVEN a conversations to be indexed
//		Conversation conversation = Conversation.builder()
//				.conversationId(prefix + "conv1")
//				.event(StartedCallEvent.builder()
//						.eventId("callrec:sid:one:started")
//						.resource(defaultResource())
//						.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//						.participant(ConversationParticipant.builder()
//								.type(AGENT)
//								.user(User.builder().userId("userIdWithAssignedFilters").build())
//								.build())
//						.build())
//				.event(TagEvent.builder()
//						.eventId("callrec:sid:one:tag")
//						.createdBy(User.builder().userId("userIdWithAssignedFilters").build())
//						.resource(defaultResource())
//						.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//						.label(Label.builder().labelId(1L).build())
//						.participant(ConversationParticipant.builder()
//								.type(AGENT)
//								.user(User.builder().userId("userIdWithAssignedFilters").build())
//								.build())
//						.build())
//				.build();
//		Conversation conversationEmail = Conversation.builder()
//				.conversationId("conversationIdFromAssignedFilter1")
//				.event(EmailEvent.builder()
//						.eventId("callrec:sid:one:email")
//						.resource(defaultResource())
//						.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//						.participant(ConversationParticipant.builder()
//								.type(AGENT)
//								.user(User.builder().userId("UUID-12").build())
//								.build())
//						.build())
//				.build();
//		Conversation conversationChat = Conversation.builder()
//				.conversationId("conversationIdFromAssignedFilter2")
//				.event(ChatEvent.builder()
//						.eventId("callrec:sid:one:chat")
//						.resource(defaultResource())
//						.start(ZonedDateTime.parse("2014-01-01T14:34:56.203+01:00"))
//						.participant(ConversationParticipant.builder()
//								.type(AGENT)
//								.user(User.builder().userId("UUID-12").build())
//								.build())
//						.build())
//				.build();
//		setupIndexedConversations(conversation, conversationEmail, conversationChat);
//
//		// GIVEN label lookup returns even more labels than the one used in the conversation
//		dataAccess.givenThat(post(urlPathEqualTo("/v3/labels/lookup"))
//				.willReturn(okJson(toJson(LabelList.builder()
//						.label(Label.builder().labelId(1L).name("label1").build())
//						.label(Label.builder().labelId(2L).name("label2").build())
//						.build()))));
//		// GIVEN encourage-data returns user assigned filter with label
//		dataAccess.givenThat(get(urlMatching("/v3/search-templates/users/.*"))
//				.willReturn(okJson(toJson(new SearchTemplateList(List.of(
//						SearchTemplate.builder().conversationSearch(
//										ClientConversationSearch.builder()
//												.label(SearchableLabel.builder().labelId(1L).build())
//												.build())
//								.build()))))));
//
//		// WHEN searching for a conversation with limited activities
//		ConversationsPage resultPage = itHelper.searchConversations(ClientConversationSearch.builder()
//				.onBehalfOf(User.builder()
//						.userId("userIdWithAssignedFilters")
//						.role(Role.builder().name(INTERACTIONS_AGENT_VIEW.name()).build())
//						.build())
//				.build());
//
//		// THEN the resulting conversation only has the events the user is able to see
//		assertThat(resultPage.getConversations()).asList().hasSize(1);
//	}

	private String groupPath(Group... groups) {
		return Arrays.stream(groups).map(group -> PATH_PREFIX + group.getGroupName())
				.collect(Collectors.joining());
	}
}

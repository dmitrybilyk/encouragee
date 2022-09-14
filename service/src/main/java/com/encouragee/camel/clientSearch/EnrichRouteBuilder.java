package com.encouragee.camel.clientSearch;

import com.encouragee.camel.clientSearch.model.ConversationEntities;
import com.encouragee.camel.clientSearch.model.ConversationSearchPermissions;
import com.encouragee.camel.clientSearch.model.ConversationUpdate;
import com.encouragee.camel.clientSearch.model.ConversationsPage;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zoomint.encourage.common.model.label.LabelList;
import com.zoomint.encourage.common.model.label.LabelLookup;
import com.zoomint.encourage.common.model.questionnaire.Questionnaire;
import com.zoomint.encourage.common.model.search.MetadataKey;
import com.zoomint.encourage.common.model.search.MetadataKeyList;
import com.zoomint.encourage.common.model.search.SearchTemplateList;
import com.zoomint.encourage.common.model.settings.FieldConfig;
import com.zoomint.encourage.common.model.settings.FieldConfigList;
import com.zoomint.encourage.common.model.speech.SpeechPhraseList;
import com.zoomint.encourage.common.model.speech.SpeechPhraseLookup;
import com.zoomint.encourage.model.QuestionnaireList;
import com.zoomint.encourage.model.conversation.Conversation;
import com.zoomint.encourage.model.conversation.ConversationEvent;
import com.zoomint.encourage.model.conversation.event.AnonymizeEvent;
import com.zoomint.encourage.model.search.ClientConversationSearch;
import com.zoomint.keycloak.provider.api.dto.UserLookup;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.apache.camel.util.toolbox.AggregationStrategies.useOriginal;

@Component
public class EnrichRouteBuilder extends RouteBuilder {
	public static final String URI_ENRICH_CONVERSATIONS = "direct:enrichConversations";
	public static final String URI_ENRICH_CONVERSATION = "direct:enrichConversation";
	public static final String URI_ENRICH_SEARCH = "direct:enrichSearch";
	public static final String URI_ENRICH_SEARCH_TEMPLATES = "direct:enrichSearchTemplates";
	public static final String URI_ENRICH_SEARCH_PERMISSIONS = "direct:enrichSearchPermissions";
	public static final String URI_ENRICH_UPDATE = "direct:enrichUpdate";
	public static final String URI_LOAD_NEW_EVENT_DATA = "direct:enrichNewEvent";

	private static final String URI_LOAD_USERS = "direct:enrichLoadUsers";
	private static final String URI_LOAD_GROUPS = "direct:enrichLoadGroups";
	private static final String URI_LOAD_LABELS = "direct:enrichLoadLabels";
	private static final String URI_LOAD_PHRASES = "direct:enrichLoadPhrases";
	private static final String URI_LOAD_QUESTIONNAIRES = "direct:enrichLoadQuestionnaires";

	private static final String URI_LOAD_ANONYMIZED_FIELDS = "direct:enrichAnonymizeEvent";

	/**
	 * Questionnaires are cached for a short while, because they change rarely,
	 * but lookup is always for the whole system.
	 */
	private final Cache<String, Questionnaire> questionnaires = Caffeine.newBuilder()
			.maximumSize(10_000)
			.expireAfterWrite(Duration.ofMinutes(5))
			.build();

	@Override
	public void configure() {
		errorHandler(noErrorHandler()); // propagate exceptions back to original route

		from(URI_ENRICH_CONVERSATIONS).routeId("enrichConversations")
				.setProperty("entities").body(ConversationEntities.class)
				.enrich(URI_LOAD_QUESTIONNAIRES, useOriginal(false), true) // ignore exceptions
				.process(exchange -> exchange.getProperty("entities", ConversationEntities.class)
						.enrichEvents(exchange.getIn().getBody(ConversationsPage.class)
								.getConversations().stream().flatMap(conv -> conv.getEvents().stream())))
		;

		from(URI_ENRICH_CONVERSATION).routeId("enrichConversation")
				.setProperty("entities").body(ConversationEntities.class)
				.enrich(URI_LOAD_QUESTIONNAIRES, useOriginal(false), true) // ignore exceptions
				.process(exchange -> exchange.getProperty("entities", ConversationEntities.class)
						.enrichEvents(exchange.getIn().getBody(Conversation.class).getEvents().stream()))
		;

		from(URI_ENRICH_SEARCH).routeId("enrichSearch")
				.setProperty("entities").body(ConversationEntities.class)
				.multicast()
					.parallelProcessing() // bulk load all entities in parallel
					.aggregationStrategy(useOriginal())
					.stopOnException()
					.stopOnAggregateException()
					.to(URI_LOAD_GROUPS, URI_LOAD_LABELS)
				.end()
				.process(exchange -> exchange.getProperty("entities", ConversationEntities.class)
						.enrichSearch(exchange.getIn().getBody(ClientConversationSearch.class)))
		;

		from(URI_ENRICH_SEARCH_TEMPLATES).routeId("enrichSearchTemplates")
				.setProperty("entities").body(ConversationEntities.class)
				.multicast()
					.parallelProcessing() // bulk load all entities in parallel
					.aggregationStrategy(useOriginal())
					.stopOnException()
					.stopOnAggregateException()
					.to(URI_LOAD_GROUPS, URI_LOAD_LABELS)
				.end()
				.process(exchange -> exchange.getProperty("entities", ConversationEntities.class)
						.enrichSearchTemplates(exchange.getIn().getBody(SearchTemplateList.class)))
		;

		from(URI_ENRICH_SEARCH_PERMISSIONS).routeId("enrichSearchPermissions")
				.setProperty("entities").body(ConversationEntities.class)
				.multicast()
					.parallelProcessing() // bulk load all entities in parallel
					.aggregationStrategy(useOriginal())
					.stopOnException()
					.stopOnAggregateException()
					.to(URI_LOAD_GROUPS)
				.end()
				.process(exchange -> exchange.getProperty("entities", ConversationEntities.class)
						.enrichSearchPermissions(exchange.getIn().getBody(ConversationSearchPermissions.class)))
		;

		from(URI_ENRICH_UPDATE).routeId("enrichUpdate")
				.setProperty("entities").body(ConversationEntities.class)
				.multicast()
					.parallelProcessing()
					.aggregationStrategy(useOriginal())
					.stopOnException()
					.stopOnAggregateException()
					.to(URI_LOAD_QUESTIONNAIRES)
					.enrichWith(KeycloakRouteBuilder.URI_LOOKUP_USER_GROUP_MEMBERSHIP)
						.body(ConversationUpdate.class, List.class, ConversationUpdate::addMemberUserRoles)
				.end()
				.process(exchange -> exchange.getProperty("entities", ConversationEntities.class)
						.enrichEvents(exchange.getIn().getBody(ConversationUpdate.class)
								.getConversations().stream().flatMap(conv -> conv.getEvents().stream())))
		;

		from(URI_LOAD_NEW_EVENT_DATA).routeId("enrichNewEventData")
				.setProperty("entities").body(ConversationEntities.class)
				.multicast()
					.parallelProcessing() // bulk load all entities in parallel
					.aggregationStrategy(useOriginal())
					.stopOnException()
					.stopOnAggregateException()
					.to(URI_LOAD_USERS, URI_LOAD_LABELS, URI_LOAD_PHRASES, URI_LOAD_QUESTIONNAIRES, URI_LOAD_ANONYMIZED_FIELDS)
				.end()
				.process(exchange -> exchange.getProperty("entities", ConversationEntities.class)
						.enrichEvents(Stream.of(exchange.getIn().getBody(ConversationEvent.class))))
		;

		from(URI_LOAD_USERS).routeId("enrichLoadUsers")
				.setBody().exchange(exchange -> exchange.getProperty("entities", ConversationEntities.class).getUserLookup())
				.filter().body(UserLookup.class, lookup -> !lookup.isEmpty())
				.to(KeycloakRouteBuilder.URI_LOOKUP_USER)
				.process(exchange -> exchange.getProperty("entities", ConversationEntities.class)
						.setUsers(exchange.getIn().getBody(List.class)))
		;

		from(URI_LOAD_GROUPS).routeId("enrichLoadGroups")
				.setBody().exchange(exchange -> exchange.getProperty("entities", ConversationEntities.class).getParentGroupUUIDs())
				.to(KeycloakRouteBuilder.URI_LOOKUP_SUBGROUPS)
				.process(exchange -> exchange.getProperty("entities", ConversationEntities.class)
						.setGroups(exchange.getIn().getBody(Set.class)))

		;

		from(URI_LOAD_LABELS).routeId("enrichLoadLabels")
				.setBody().exchange(exchange -> exchange.getProperty("entities", ConversationEntities.class).getLabelLookup())
				.filter().body(LabelLookup.class, lookup -> !lookup.isEmpty())
				.to(DataAccessRouteBuilder.URI_LOOKUP_LABELS)
				.process(exchange -> exchange.getProperty("entities", ConversationEntities.class)
						.setLabels(exchange.getIn().getBody(LabelList.class).getLabels()))
		;

		from(URI_LOAD_PHRASES).routeId("enrichLoadPhrases")
				.setBody().exchange(exchange -> exchange.getProperty("entities", ConversationEntities.class).getPhraseLookup())
				.filter().body(SpeechPhraseLookup.class, lookup -> !lookup.isEmpty())
				.to(DataAccessRouteBuilder.URI_LOOKUP_SPEECH_PHRASES)
				.process(exchange -> exchange.getProperty("entities", ConversationEntities.class)
						.setPhrases(exchange.getIn().getBody(SpeechPhraseList.class).getSpeechPhrases()))
		;

		from(URI_LOAD_QUESTIONNAIRES).routeId("enrichLoadQuestionnaires")
				.filter(exchange -> checkQuestionnaireCacheMisses(exchange.getProperty("entities", ConversationEntities.class)))
				.to(ZQMConnectorRouteBuilder.URI_GET_QUESTIONNAIRES)
				.process(this::updateQuestionnaireCache)
		;

		from(URI_LOAD_ANONYMIZED_FIELDS).routeId("enrichAnonymizeEvent")
				.filter(body().isInstanceOf(AnonymizeEvent.class))
				.enrichWith(DataAccessRouteBuilder.URI_GET_METADATA_KEYS_TO_ANONYMIZE)
					.body(AnonymizeEvent.class, MetadataKeyList.class, this::setMetadataKeys)
				.enrichWith(DataAccessRouteBuilder.URI_GET_FIELD_CONFIG_TO_ANONYMIZE)
					.body(AnonymizeEvent.class, FieldConfigList.class, this::setFieldConfigs)
		;
	}

	private boolean checkQuestionnaireCacheMisses(ConversationEntities entities) {
		Map<String, Questionnaire> cacheHits = questionnaires.getAllPresent(entities.getQuestionnaireExternalIds());
		boolean allCached = cacheHits.size() == entities.getQuestionnaireExternalIds().size();
		if (allCached) {
			// everything is cached - just use the cache
			entities.setQuestionnaires(cacheHits);
			return false;
		} else {
			// there are cache misses - reload cache first
			return true;
		}
	}

	private void updateQuestionnaireCache(Exchange exchange) {
		exchange.getIn().getBody(QuestionnaireList.class).getQuestionnaires()
				.forEach(this::addToCache);

		ConversationEntities entities = exchange.getProperty("entities", ConversationEntities.class);
		Map<String, Questionnaire> cacheHits = questionnaires.getAllPresent(entities.getQuestionnaireExternalIds());
		entities.setQuestionnaires(cacheHits); // get all entities from the now-loaded cache
	}

	private void addToCache(com.zoomint.encourage.model.Questionnaire questionnaire) {
		Questionnaire value = Questionnaire.builder()
				.name(questionnaire.getName())
				.version(questionnaire.getVersion())
				.scoringSystem(questionnaire.getScoringSystem())
				.build();
		questionnaires.put(questionnaire.getId(), value);
	}

	private AnonymizeEvent setMetadataKeys(AnonymizeEvent anonymizeEvent, MetadataKeyList metadataKeys) {
		anonymizeEvent.setMetadataKeys(metadataKeys.getMetadataKeys().stream().map(MetadataKey::getKey).collect(toSet()));
		log.debug("AnonymizeEvent enriched with metadata keys: {}", anonymizeEvent.getMetadataKeys());
		return anonymizeEvent;
	}

	private AnonymizeEvent setFieldConfigs(AnonymizeEvent anonymizeEvent, FieldConfigList fieldConfig) {
		anonymizeEvent.setFields(fieldConfig.getFieldConfigs().stream().map(FieldConfig::getField).collect(toSet()));
		log.debug("AnonymizeEvent enriched with field configuration: {}", anonymizeEvent.getFields());
		return anonymizeEvent;
	}
}

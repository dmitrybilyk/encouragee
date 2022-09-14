package com.encouragee.camel.clientSearch.conversation.converter;

import com.encouragee.camel.clientSearch.model.ConversationSearchPermissions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.zoomint.encourage.common.exceptions.accelerator.ExceptionMessage;
import com.zoomint.encourage.common.exceptions.security.AuthorizationException;
import com.zoomint.encourage.common.model.label.Label;
import com.zoomint.encourage.common.model.label.LabelType;
import com.zoomint.encourage.model.conversation.ConversationParticipantType;
import com.zoomint.encourage.model.conversation.ConversationResource;
import com.zoomint.encourage.model.search.ClientConversationSearch;
import com.zoomint.encourage.model.search.SearchableContact.Match;
import com.zoomint.encourage.model.search.SearchableContact.Party;
import com.zoomint.encourage.model.search.SearchableLabel;
import com.zoomint.encourage.model.search.SearchableMetadata;
import com.zoomint.encourage.model.search.SearchableReview.ReviewScoringSystem;
import com.zoomint.encourage.model.search.SearchableSurvey.SurveyScoringSystem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.encouragee.camel.clientSearch.model.ConversationDocument.*;
import static com.zoomint.encourage.model.search.SearchableMetadata.CompareOperator.NUMBER_OPERATORS;
import static java.time.LocalTime.MIDNIGHT;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
import static java.util.Comparator.*;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.solr.client.solrj.util.ClientUtils.escapeQueryChars;
import static org.springframework.data.solr.core.query.AnyCriteria.any;
import static org.springframework.data.solr.core.query.Criteria.where;

@Component
public class ClientConversationSearchConverter {

	private static final Criteria WILDCARD_CRITERIA = any();
	private static final Criteria NEGATIVE_CRITERIA = any().notOperator();

	private static final int CONFIDENCE_MIN = 0;
	private static final int CONFIDENCE_MAX = 100;

	private static final int SAMPLE_PRECISION = 6; // note: very high precision results in more complicated Regex, which Solr refuses to handle
	private static final BigDecimal SAMPLE_ALL = new BigDecimal(100);
	private static final BigDecimal SAMPLE_PERCENTAGE_BASE = BigDecimal.valueOf(16).pow(SAMPLE_PRECISION).divide(SAMPLE_ALL);

	private static final ZoneOffset STORE_ZONE_OFFSET = ZoneOffset.UTC;
	private static final ImmutableSet<DayOfWeek> DAYS_OF_WEEK = ImmutableSet.copyOf(DayOfWeek.values());

	private static final ImmutableMap<ReviewScoringSystem, List<String>> REVIEW_SCORING_FIELDS = ImmutableMap.of(
			ReviewScoringSystem.ANY, ImmutableList.of(FIELD_REVIEW_SCORE_PERCENTAGE, FIELD_REVIEW_SCORE_POINTS),
			ReviewScoringSystem.PERCENTAGE, ImmutableList.of(FIELD_REVIEW_SCORE_PERCENTAGE),
			ReviewScoringSystem.POINTS, ImmutableList.of(FIELD_REVIEW_SCORE_POINTS));

	private static final ImmutableMap<SurveyScoringSystem, List<String>> SURVEY_SCORING_FIELDS = ImmutableMap.of(
			SurveyScoringSystem.ANY, ImmutableList.of(FIELD_SURVEY_SCORE_PERCENTAGE, FIELD_SURVEY_SCORE_POINTS),
			SurveyScoringSystem.PERCENTAGE, ImmutableList.of(FIELD_SURVEY_SCORE_PERCENTAGE),
			SurveyScoringSystem.POINTS, ImmutableList.of(FIELD_SURVEY_SCORE_POINTS));

	private static final ImmutableMap<Party, List<String>> CONTACTS_SEARCH_FIELDS = ImmutableMap.of(
			Party.FROM, ImmutableList.of(FIELD_CONTACTS_FROM),
			Party.TO, ImmutableList.of(FIELD_CONTACTS_TO),
			Party.ANY, ImmutableList.of(FIELD_CONTACTS_FROM, FIELD_CONTACTS_TO));

	private static final ImmutableMap<Match, Function<String, String>> CONTACTS_SEARCH_EXPRESSION_BUILDERS = ImmutableMap.of(
			Match.START, contact -> "(" + escapeQueryChars(contact) + "*)",
			Match.END, contact -> "(*" + escapeQueryChars(contact) + ")",
			Match.EXACT, contact -> "(" + escapeQueryChars(contact) + ")");

	private final RegexSampler sampler = new RegexSampler();

	@NotNull
	public Optional<Criteria> getFulltextCriteria(@NotNull ClientConversationSearch clientSearch) {
		return Optional.ofNullable(clientSearch.getFulltext())
				.map(SolrStringHelper::sanitizeString)
				.filter(StringUtils::hasText)
				.map(expression -> "(" + expression + ")")
				.map(expression -> where(FIELD_CONVERSATION_ID).expression(expression + "^=" + 2000.0f)
						.or(where(FIELD_PREVIOUS_IDS).expression(expression + "^=" + 2000.0f))
						// comments are not in all docs, so their score is low without boost
						.or(where(FIELD_COMMENTS).expression(expression + "^=" + 1000.0f))
						// score higher if more matching comments
						.or(where(FIELD_COMMENTS).expression(expression)).boost(2.0f)
						// survey comments are not in all docs either
						.or(where(FIELD_SURVEY_COMMENTS).expression(expression + "^=" + 100.0f))
						.or(where(FIELD_SURVEY_COMMENTS).expression(expression)).boost(2.0f)
						.or(where(FIELD_CONTACTS_FROM).expression(expression).boost(5.0f))
						.or(where(FIELD_CONTACTS_TO).expression(expression).boost(5.0f))
						.or(where(FIELD_TEXT_SUBJECT).expression(expression))
						.or(where(FIELD_TEXT_BODY).expression(expression).boost(0.5f)));
	}

	@NotNull
	public Stream<Criteria> getFilterCriteria(
			@NotNull ClientConversationSearch clientSearch,
			@NotNull ConversationSearchPermissions filter,
			@NotNull ZonedDateTime clientTimestamp) {

		Stream<Criteria> permissionCriteria = getPermissionsCriteria(filter);
		Stream<Criteria> searchCriteria = getSearchCriteria(clientSearch, clientTimestamp);

		return Stream.concat(permissionCriteria, searchCriteria);
	}

	@NotNull
	public Stream<Criteria> getPermissionsCriteria(@NotNull ConversationSearchPermissions filter) {
		if (filter.isAllowAll()) {
			return Stream.empty();
		}

		Criteria criteria = Stream.of(
				Optional.ofNullable(filter.getAllowedUserId()).map(userId -> where(FIELD_USER_UUIDS).is(userId)),
				Optional.ofNullable(filter.getAllowedGroupUUIDs())
						.filter(groupUUIDs -> !groupUUIDs.isEmpty())
						.map(groupUUIDs -> where(FIELD_GROUP_UUIDS).in(groupUUIDs))
		)
				.flatMap(Optional::stream)
				.reduce(Criteria::or)
				.orElseThrow(() -> new AuthorizationException("User has incorrect permissions")); // no permissions => search is unauthorized

		return Stream.of(criteria);
	}

	@NotNull
	public Stream<Criteria> getSearchCriteria(ClientConversationSearch clientSearch, ZonedDateTime clientTimestamp) {
		return Stream.of(
				createConversationIdCriteria(clientSearch),
				createDateTimeCriteria(clientSearch, clientTimestamp),
				createSearchableDaysCriteria(clientSearch, clientTimestamp),
				createDurationCriteria(clientSearch),
				createDirectionCriteria(clientSearch),
				createCommunicationTypesCriteria(clientSearch),
				createUserIdsCriteria(clientSearch),
				createGroupIdsCriteria(clientSearch),
				createMetadataCriteria(clientSearch),
				// add fulltext clientSearch to avoid invalid binary logic
				createFullTextCriteria(clientSearch),
				createCustomCriteria(clientSearch),
				createCorrelationIdCriteria(clientSearch),
				createLabelCriteria(clientSearch),
				createContactsCriteria(clientSearch),
				createNumberOfSegmentsCriteria(clientSearch),
				createNumberOfAgentsCriteria(clientSearch),
				createReviewCriteria(clientSearch),
				createSurveyCriteria(clientSearch),
				createResourceCriteria(clientSearch),
				createSampleCriteria(clientSearch))
				.flatMap(identity());
	}

	private Stream<Criteria> createConversationIdCriteria(@NotNull ClientConversationSearch search) {
		return Optional.ofNullable(search.getConversationId())
				.filter(StringUtils::hasText)
				.map(conversationId -> where(FIELD_CONVERSATION_ID).is(conversationId)
						.or(where(FIELD_PREVIOUS_IDS).is(conversationId)))
				.map(Criteria::connect)
				.stream();
	}

	private Stream<Criteria> createDateTimeCriteria(@NotNull ClientConversationSearch clientSearch, ZonedDateTime clientTimestamp) {
		return Stream.of(where(FIELD_START_DATE_TIME).between(
				Optional.ofNullable(clientSearch.getPeriod())
						.map(period -> period.getStart(clientTimestamp))
						.map(ZonedDateTime::toInstant)
						.orElse(null),
				Optional.ofNullable(clientSearch.getPeriod())
						.map(period -> period.getEnd(clientTimestamp))
						.filter(clientTimestamp::isAfter) // do not search in the future
						.map(ZonedDateTime::toInstant)
						.orElseGet(clientTimestamp::toInstant),
				true, false));
	}

	private Stream<Criteria> createSearchableDaysCriteria(@NotNull ClientConversationSearch clientSearch, ZonedDateTime clientTimestamp) {
		Set<DayOfWeek> daysOfWeek = Optional.ofNullable(clientSearch.getDaysOfWeek())
				.filter(dayOfWeeks -> !dayOfWeeks.isEmpty())
				.orElse(DAYS_OF_WEEK);

		LocalTime timeOfDayFrom = Optional.ofNullable(clientSearch.getTimeOfDayFrom()).orElse(MIDNIGHT);
		LocalTime timeOfDayTo = Optional.ofNullable(clientSearch.getTimeOfDayTo()).orElse(MIDNIGHT);

		if (DAYS_OF_WEEK.size() == daysOfWeek.size()
				&& MIDNIGHT.equals(timeOfDayFrom)
				&& MIDNIGHT.equals(timeOfDayTo)) {
			return Stream.empty(); // no restrictions
		}

		// use fixed offset instead of geographical timezone to ignore DST, since this could apply to any virtually date
		ZoneOffset clientTimeOffset = clientTimestamp.getOffset();

		// get start/end time at client offset
		OffsetTime startTimeStored = timeOfDayFrom.atOffset(clientTimeOffset);
		OffsetTime endTimeStored = timeOfDayTo.atOffset(clientTimeOffset);

		List<SearchableDay> searchableDays;
		if (DAYS_OF_WEEK.size() == daysOfWeek.size()) {
			searchableDays = toSearchableDays(clientTimestamp.getDayOfWeek(), startTimeStored, endTimeStored) // simplify by calculating for just one day
					.peek(day -> day.setDayOfWeek(null)) // and then use null to specify all days
					.sorted()
					.filter(new SearchableDayMerger())
					.collect(toList());
		} else {
			searchableDays = daysOfWeek.stream()
					.flatMap(dayOfWeek -> toSearchableDays(dayOfWeek, startTimeStored, endTimeStored))
					.sorted()
					.filter(new SearchableDayMerger())
					.collect(toList());
		}

		return searchableDays.stream()
				.flatMap(searchableDay -> getCriteriaForSearchableDay(searchableDay)
						.reduce(Criteria::and)
						//rather use brackets for (A AND B) OR C, otherwise A AND B may not have priority
						//why this bracket here? because spring data solr joins (A AND B) with (C AND D) using OR
						//resulting in (A AND B OR (C AND D)) - which in solr case of "boolean" logic is not what we want
						.map(Criteria::connect)
						.stream())
				.reduce(Criteria::or)
				.map(Criteria::connect)
				.stream();
	}

	@NotNull
	private Stream<Criteria> getCriteriaForSearchableDay(@NotNull SearchableDay searchableDay) {
		return Stream.of(
				searchableDay.getDayOfWeek() != null
						? where(FIELD_DAY_OF_WEEK).is(searchableDay.getDayOfWeek().getValue())
						: null,
				searchableDay.getSecondFrom() != null || searchableDay.getSecondTo() != null
						// lower bound included, upper bound excluded, supports null
						? where(FIELD_SECOND_OF_DAY).between(searchableDay.getSecondFrom(), searchableDay.getSecondTo(), true, false)
						: null)
				.filter(Objects::nonNull);
	}

	@NotNull
	private Stream<Criteria> createDurationCriteria(@NotNull ClientConversationSearch search) {
		if (search.getLongerThan() == null && search.getShorterThan() == null) {
			return Stream.empty();
		}

		// to ignore conversations with zero duration, the query should not be open ended on the left side
		Long from = Optional.ofNullable(search.getLongerThan()).map(TimeUnit.SECONDS::toMillis).orElse(1L);
		Long to = Optional.ofNullable(search.getShorterThan()).map(TimeUnit.SECONDS::toMillis).orElse(null);
		return Stream.of(where(FIELD_DURATION).between(from, to));
	}

	@NotNull
	private Stream<Criteria> createDirectionCriteria(@NotNull ClientConversationSearch search) {
		if (search.getDirections() == null || search.getDirections().isEmpty()) {
			return Stream.empty();
		}
		return Stream.of(where(FIELD_DIRECTION).in(search.getDirections()));
	}

	@NotNull
	private Stream<Criteria> createCommunicationTypesCriteria(@NotNull ClientConversationSearch search) {
		Stream<Criteria> includeCriteria = search.getCommunicationTypeIncludes().stream()
				// for OR just use Criteria.in()
				.map(value -> where(FIELD_COMMUNICATION_TYPES).is(value));

		Stream<Criteria> excludeCriteria = Optional.of(search.getCommunicationTypeExcludes())
				.filter(communicationTypes -> !communicationTypes.isEmpty())
				.map(communicationTypes -> where(FIELD_COMMUNICATION_TYPES).not().in(communicationTypes))
				.stream();

		return Stream.concat(includeCriteria, excludeCriteria);
	}

	private Stream<Criteria> createUserIdsCriteria(@NotNull ClientConversationSearch search) {
		return search.getUserUUIDs().stream()
				.map(userUUId -> where(FIELD_USER_UUIDS).is(userUUId)); // AND
	}

	private Stream<Criteria> createGroupIdsCriteria(@NotNull ClientConversationSearch search) {
		return Optional.ofNullable(search.getGroupUUIDs())
				.filter(groupUUIDs -> !groupUUIDs.isEmpty())
				.map(groupUUIDs -> where(FIELD_GROUP_UUIDS).in(groupUUIDs)) // OR
				.stream();
	}

	private Stream<Criteria> createMetadataCriteria(@NotNull ClientConversationSearch search) {
		return search.getMetadata().stream()
				.map(metadata -> buildMetadataCriteria(metadata)
						.map(Criteria::connect)
						.reduce(Criteria::or)
						.map(Criteria::connect))
				.filter(Optional::isPresent)
				.map(Optional::get);
	}

	private Stream<Criteria> buildMetadataCriteria(SearchableMetadata metadata) {
		validateMetadataSearch(metadata);

		String key = escapeQueryChars(metadata.getKey());
		if (metadata.getValues().stream().anyMatch(Strings::isNullOrEmpty)) {
			throw new IllegalArgumentException(String.format("%s key can not have empty values", key));
		}
		switch (metadata.getCompareOperator()) {
			case STARTS_WITH:
				return metadata.getValues().stream()
						.map(this::sanitizeMetadataValue)
						.map(value -> where(FieldNames.getMetadataFieldName(key)).expression("/" + value + ".*/"));
			case CONTAINS:
				return metadata.getValues().stream()
						.map(this::sanitizeMetadataValue)
						.map(value -> where(FieldNames.getMetadataFieldName(key)).expression("/.*" + value + ".*/"));
			case EQUALS:
				return Stream.of(where(FieldNames.getMetadataFieldName(key)).in(toLowercaseValues(metadata.getValues())));
			case GREATER_OR_EQUAL:
				return Stream.of(where(FieldNames.getNumberMetadataFieldName(key)).greaterThanEqual(metadata.getValues().iterator().next()));
			case GREATER_THAN:
				return Stream.of(where(FieldNames.getNumberMetadataFieldName(key)).greaterThan(metadata.getValues().iterator().next()));
			case LESS_OR_EQUAL:
				return Stream.of(where(FieldNames.getNumberMetadataFieldName(key)).lessThanEqual(metadata.getValues().iterator().next()));
			case LESS_THAN:
				return Stream.of(where(FieldNames.getNumberMetadataFieldName(key)).lessThan(metadata.getValues().iterator().next()));
			default:
				throw new IllegalArgumentException("unknown operator: " + metadata.getCompareOperator());
		}
	}

	private Stream<Criteria> createFullTextCriteria(@NotNull ClientConversationSearch search) {
		return Optional.ofNullable(search.getFulltext())
				.map(SolrStringHelper::sanitizeString)
				.filter(StringUtils::hasText)
				.stream()
				.flatMap(expression -> Stream.of(
						where(FIELD_CONTACTS_FROM).expression("(" + expression + ")"),
						where(FIELD_CONTACTS_TO).expression("(" + expression + ")"),
						where(FIELD_COMMENTS).expression("(" + expression + ")"),
						where(FIELD_SURVEY_COMMENTS).expression("(" + expression + ")"),
						where(FIELD_TEXT_SUBJECT).expression("(" + expression + ")"),
						where(FIELD_TEXT_BODY).expression("(" + expression + ")"),
						where(FIELD_CONVERSATION_ID).is(search.getFulltext()),
						where(FIELD_PREVIOUS_IDS).is(search.getFulltext())
				))
				.map(Criteria::connect)
				.reduce(Criteria::or)
				.map(Criteria::connect)
				.stream();
	}

	private Stream<Criteria> createCustomCriteria(@NotNull ClientConversationSearch search) {
		if (!StringUtils.isEmpty(search.getCustomSearch())) {
			return Stream.of(new SimpleStringCriteria(search.getCustomSearch()));
		}
		return Stream.empty();
	}

	private Stream<Criteria> createCorrelationIdCriteria(ClientConversationSearch clientSearch) {
		if (CollectionUtils.isEmpty(clientSearch.getCorrelationIds())) {
			return Stream.empty();
		}
		return Stream.of(where(FIELD_CORRELATION_IDS).in(clientSearch.getCorrelationIds()));
	}

	private Stream<Criteria> createLabelCriteria(ClientConversationSearch clientSearch) {
		if (CollectionUtils.isEmpty(clientSearch.getLabels())) {
			return Stream.empty();
		}

		List<Criteria> andCriteria = new ArrayList<>();
		List<Criteria> orCriteria = new ArrayList<>();

		for (SearchableLabel searchableLabel : clientSearch.getLabels()) {
			//for label, find all phrases and for all of them build query
			Label label = searchableLabel.getLabel();
			if (label.getType() == LabelType.SPEECH_TAG) {
				validateLabelSearch(searchableLabel);

				// Note: when search eventually contains participants, use those instead!
				Optional<Criteria> phrasesCriteria = Arrays.stream(ConversationParticipantType.values())
						.flatMap(participantType -> label.getSpeechPhrases().stream()
								.map(speechPhrase -> where(FieldNames.getPhraseFieldName(speechPhrase.getSpeechPhraseId(), participantType))
										.between(searchableLabel.getConfidenceFrom(), searchableLabel.getConfidenceTo())))
						.reduce(Criteria::or);
				if (searchableLabel.isExclude()) {
					// only add if actually excluding something
					// Solr needs something positive, so *:* -criteria
					phrasesCriteria.ifPresent(excluded -> andCriteria.add(WILDCARD_CRITERIA.and(excluded.notOperator())));
				} else {
					andCriteria.add(phrasesCriteria.orElse(NEGATIVE_CRITERIA));
				}
			} else {
				if (searchableLabel.getConfidenceFrom() != CONFIDENCE_MIN || searchableLabel.getConfidenceTo() != CONFIDENCE_MAX) {
					// verify confidence is not provided to fail-fast on invalid API usage
					throw new IllegalArgumentException(String.format("Label %s \"%s\" does not support confidence",
							searchableLabel.getLabel().getLabelId(), searchableLabel.getLabel().getName()));
				}
				orCriteria.add(where(FIELD_LABELS).is(searchableLabel.getLabel().getLabelId()));
				// or allow matching by KV pair instead of label ID
				if (label.getDataKey() != null && label.getDataValue() != null) {
					orCriteria.add(where(FieldNames.getMetadataFieldName(escapeQueryChars(label.getDataKey())))
							.is(label.getDataValue().toLowerCase(Locale.ROOT)));
				}
			}
		}

		orCriteria.stream()
				.map(Criteria::connect)
				.reduce(Criteria::or)
				.ifPresent(andCriteria::add);

		if (andCriteria.isEmpty()) {
			return Stream.empty();
		}

		// connect the first, the operator AND will connect the rest
		andCriteria.set(0, andCriteria.get(0).connect());
		return andCriteria.stream();
	}

	private Stream<Criteria> createContactsCriteria(ClientConversationSearch clientSearch) {
		if (clientSearch.getContacts() == null) {
			return Stream.empty();
		}

		return clientSearch.getContacts().stream()
				.flatMap(contact -> {
					Assert.notNull(contact.getTerms(), "Contact terms must not be null");
					List<String> fields = CONTACTS_SEARCH_FIELDS.get(contact.getParty());
					Assert.notNull(fields, "Unknown contact party: " + contact.getParty());
					Function<String, String> expressionBuilder = CONTACTS_SEARCH_EXPRESSION_BUILDERS.get(contact.getMatch());
					Assert.notNull(expressionBuilder, "Unknown contact match: " + contact.getMatch());
					return contact.getTerms().stream()
							.map(expressionBuilder)
							.flatMap(expression -> fields.stream()
									.map(field -> where(field).expression(expression)))
							.reduce(Criteria::or)
							.stream();
				});
	}

	private Stream<Criteria> createNumberOfAgentsCriteria(ClientConversationSearch clientSearch) {
		if (clientSearch.getAgents() == null) {
			return Stream.empty();
		}

		return Stream.of(where(FIELD_AGENTS_COUNT).between(clientSearch.getAgents().getMin(), clientSearch.getAgents().getMax()));
	}

	private Stream<Criteria> createNumberOfSegmentsCriteria(ClientConversationSearch clientSearch) {
		if (clientSearch.getSegments() == null) {
			return Stream.empty();
		}

		return Stream.of(where(FIELD_SEGMENTS_COUNT).between(clientSearch.getSegments().getMin(), clientSearch.getSegments().getMax()));
	}

	private Stream<Criteria> createReviewCriteria(ClientConversationSearch clientSearch) {
		if (clientSearch.getReviews() == null) {
			return Stream.empty();
		}
		return clientSearch.getReviews().stream()
				.flatMap(searchableReview -> {
					List<Criteria> criteria = new ArrayList<>();

					Optional.ofNullable(searchableReview.getReviewId())
							.filter(StringUtils::hasText)
							.map(reviewId -> where(FIELD_REVIEW_IDS).is(reviewId))
							.ifPresent(criteria::add);
					Optional.ofNullable(searchableReview.getQuestionnaireId())
							.filter(StringUtils::hasText)
							.map(questionnaireId -> where(FIELD_REVIEW_QUESTIONNAIRE_IDS).is(questionnaireId))
							.ifPresent(criteria::add);
					Optional.ofNullable(searchableReview.getReviewerUUID())
							.map(reviewerUserUUID -> where(FIELD_REVIEWER_USER_UUIDS).is(reviewerUserUUID))
							.ifPresent(criteria::add);
					Optional.ofNullable(searchableReview.getRevieweeUUID())
							.map(reviewedUserUUID -> where(FIELD_REVIEWED_USER_UUIDS).is(reviewedUserUUID))
							.ifPresent(criteria::add);

					ReviewScoringSystem scoringSystem = searchableReview.getScoringSystem() != null
							? searchableReview.getScoringSystem()
							: ReviewScoringSystem.ANY;

					if (scoringSystem != ReviewScoringSystem.ANY) {
						criteria.add(where(FIELD_REVIEW_SCORING_SYSTEMS).is(searchableReview.getScoringSystem()));
					}

					if (searchableReview.getScoreMin() != null || searchableReview.getScoreMax() != null) {
						List<String> fields = REVIEW_SCORING_FIELDS.get(scoringSystem);
						Assert.notNull(fields, () -> "Search by score is not supported for scoring system: " + scoringSystem);
						fields.stream()
								.map(field -> where(field).between(searchableReview.getScoreMin(), searchableReview.getScoreMax()))
								.reduce(Criteria::or)
								.ifPresent(criteria::add);
					}

					if (criteria.isEmpty()) {
						// fallback to requiring at least one review via reviewId field
						criteria.add(where(FIELD_REVIEW_IDS).isNotNull());
					}

					return searchableReview.isExclude() ? exclude(criteria.stream()) : criteria.stream();
				});
	}

	private Stream<Criteria> createSurveyCriteria(ClientConversationSearch clientSearch) {
		if (clientSearch.getSurveys() == null) {
			return Stream.empty();
		}
		return clientSearch.getSurveys().stream()
				.flatMap(searchableSurvey -> {
					List<Criteria> criteria = new ArrayList<>();

					Optional.ofNullable(searchableSurvey.getSurveyId())
							.filter(StringUtils::hasText)
							.map(surveyId -> where(FIELD_SURVEY_IDS).is(surveyId))
							.ifPresent(criteria::add);
					Optional.ofNullable(searchableSurvey.getQuestionnaireId())
							.filter(StringUtils::hasText)
							.map(questionnaireId -> where(FIELD_SURVEY_QUESTIONNAIRE_IDS).is(questionnaireId))
							.ifPresent(criteria::add);
					Optional.ofNullable(searchableSurvey.getSurveyedUserUUID())
							.map(surveyedUserUUID -> where(FIELD_SURVEYED_USER_UUIDS).is(surveyedUserUUID))
							.ifPresent(criteria::add);
					Optional.ofNullable(searchableSurvey.getSurveyedUserUUID())
							.map(surveyedUserUUID -> where(FIELD_SURVEYED_USER_UUIDS).is(surveyedUserUUID))
							.ifPresent(criteria::add);

					SurveyScoringSystem scoringSystem = searchableSurvey.getScoringSystem() != null
							? searchableSurvey.getScoringSystem()
							: SurveyScoringSystem.ANY;

					if (scoringSystem != SurveyScoringSystem.ANY) {
						criteria.add(where(FIELD_SURVEY_SCORING_SYSTEMS).is(searchableSurvey.getScoringSystem()));
					}

					if (searchableSurvey.getScoreMin() != null || searchableSurvey.getScoreMax() != null) {
						List<String> fields = SURVEY_SCORING_FIELDS.get(scoringSystem);
						Assert.notNull(fields, () -> "Search by score is not supported for scoring system: " + scoringSystem);
						fields.stream()
								.map(field -> where(field).between(searchableSurvey.getScoreMin(), searchableSurvey.getScoreMax()))
								.reduce(Criteria::or)
								.ifPresent(criteria::add);
					}

					if (criteria.isEmpty()) {
						// fallback to requiring at least one survey via surveyId field
						criteria.add(where(FIELD_SURVEY_IDS).isNotNull());
					}

					return searchableSurvey.isExclude() ? exclude(criteria.stream()) : criteria.stream();
				});
	}

	private Stream<Criteria> createResourceCriteria(ClientConversationSearch clientSearch) {
		if (clientSearch.getResourceFlags() == ClientConversationSearch.ResourceFlags.REVIEWABLE) {
			return Stream.of(where(FIELD_RESOURCE_FLAGS).in(ConversationResource.FLAG_NO_PROBLEM));
		}
		return Stream.empty();
	}

	private Stream<Criteria> createSampleCriteria(ClientConversationSearch clientSearch) {
		if (clientSearch.getSampling() == null
				|| clientSearch.getSampling().getPercentage() == null
				|| clientSearch.getSampling().getPercentage().compareTo(SAMPLE_ALL) >= 0) {
			// ignore no sampling or >=100% sampling
			return Stream.empty();
		}

		BigInteger max = SAMPLE_PERCENTAGE_BASE
				.multiply(clientSearch.getSampling().getPercentage())
				.toBigInteger(); // ignore fractions

		if (max.compareTo(BigInteger.ZERO) <= 0) {
			throw new IllegalArgumentException("sampling percentage is too low to produce any results");
		}

		String hexPadded = String.format("%0" + SAMPLE_PRECISION + "x", max);  // convert to hex digits, pad with 0
		String regexp = sampler.createHexRegexp(hexPadded);

		return Stream.of(where(FIELD_CONVERSATION_ID).expression("/.*(" + regexp + ")/"));
	}

	private Stream<SearchableDay> toSearchableDays(DayOfWeek dayOfWeek, OffsetTime startTime, OffsetTime endTime) {
		// find any date that corresponds to the required day of week - we can ignore DST when working with fixed offsets
		LocalDate dayOfWeekDate = LocalDate.now().with(previousOrSame(dayOfWeek));

		// calculate the client start/end time and change these times to the stored timezone
		OffsetDateTime startAtDayOfWeek = dayOfWeekDate.atTime(startTime).withOffsetSameInstant(STORE_ZONE_OFFSET);
		OffsetDateTime endAtDayOfWeek = dayOfWeekDate.atTime(endTime).withOffsetSameInstant(STORE_ZONE_OFFSET);
		if (!endAtDayOfWeek.isAfter(startAtDayOfWeek)) {
			endAtDayOfWeek = endAtDayOfWeek.plusDays(1); // end is always considered to be after start
		}

		if (startAtDayOfWeek.getDayOfWeek() == endAtDayOfWeek.getDayOfWeek()) {
			// the period is a single day of the week
			return Stream.of(SearchableDay.builder()
					.dayOfWeek(startAtDayOfWeek.getDayOfWeek())
					.secondFrom(startAtDayOfWeek.toLocalTime().toSecondOfDay())
					.secondTo(endAtDayOfWeek.toLocalTime().toSecondOfDay())
					.build());
		}

		// period is split into two days of the week
		return Stream.of(
				SearchableDay.builder()
						.dayOfWeek(startAtDayOfWeek.getDayOfWeek())
						.secondFrom(startAtDayOfWeek.toLocalTime().toSecondOfDay())
						.build(),
				SearchableDay.builder()
						.dayOfWeek(endAtDayOfWeek.getDayOfWeek())
						.secondTo(endAtDayOfWeek.toLocalTime().toSecondOfDay())
						.build());
	}

	private void validateLabelSearch(@NotNull SearchableLabel speechTagLabel) {
		if (speechTagLabel.getConfidenceFrom() < CONFIDENCE_MIN || speechTagLabel.getConfidenceFrom() > CONFIDENCE_MAX
				|| speechTagLabel.getConfidenceTo() < CONFIDENCE_MIN || speechTagLabel.getConfidenceTo() > CONFIDENCE_MAX) {
			throw new IllegalArgumentException(ExceptionMessage.SEARCH_CONFIDENCE_PARAMETERS_RANGE.getMessage());
		}
		if (speechTagLabel.getConfidenceFrom() > speechTagLabel.getConfidenceTo()) {
			throw new IllegalArgumentException(ExceptionMessage.SEARCH_CONFIDENCE_PARAMETERS.getMessage());
		}
	}

	private void validateMetadataSearch(SearchableMetadata metadata) {
		if (NUMBER_OPERATORS.contains(metadata.getCompareOperator()) && metadata.getValues().size() > 1) {
			throw new IllegalArgumentException("For number operators there must be only 1 value");
		}
		if (NUMBER_OPERATORS.contains(metadata.getCompareOperator()) &&
				!NUMBER_PATTERN.matcher(metadata.getValues().iterator().next()).matches()) {
			throw new IllegalArgumentException("Metadata value must be a number if used with a number operator");
		}
	}

	private String sanitizeMetadataValue(String value) {
		return value.toLowerCase(Locale.ROOT).replaceAll("(.)", "\\\\$1");
	}

	private Set<String> toLowercaseValues(Collection<String> values) {
		return values.stream().map(value -> value.toLowerCase(Locale.ROOT)).collect(toSet());
	}

	@NotNull
	private Stream<Criteria> exclude(Stream<Criteria> criteria) {
		return criteria
				.reduce(Criteria::and)
				.map(Criteria::notOperator)
				.map(WILDCARD_CRITERIA::and)
				.map(Criteria::connect)
				.stream();
	}

	@Data
	@Builder(toBuilder = true)
	@NoArgsConstructor
	@AllArgsConstructor(access = PRIVATE)
	private static class SearchableDay implements Comparable<SearchableDay> {

		private static final Comparator<SearchableDay> DEFAULT_SORT_ORDER =
				comparing(SearchableDay::getDayOfWeek, nullsFirst(naturalOrder()))
						.thenComparing(SearchableDay::getSecondFrom, nullsFirst(naturalOrder()))
						.thenComparing(SearchableDay::getSecondTo, nullsLast(naturalOrder()));

		private DayOfWeek dayOfWeek;
		private Integer secondFrom;
		private Integer secondTo;

		@Override
		public int compareTo(@NotNull SearchableDay other) {
			return DEFAULT_SORT_ORDER.compare(this, other);
		}
	}

	/**
	 * Stateful mutating filter that can marge two SearchableDay instances
	 * by modifying the first one and skipping the second one.
	 * IMPORTANT: Requires sorted linear stream to function properly.
	 */
	private static class SearchableDayMerger implements Predicate<SearchableDay> {
		private SearchableDay previous;

		@Override
		public boolean test(SearchableDay current) {
			if (Objects.equals(current.getSecondTo(), 0)) {
				// empty SearchableDay, since secondTo field is exclusive => ignore
				return false;
			}
			if (previous != null
					// consider merging only if the same day of the week
					&& previous.getDayOfWeek() == current.getDayOfWeek()
					// consider merging only if it directly continues the previous
					&& Objects.equals(previous.getSecondTo(), current.getSecondFrom())) {
				// merge this SearchableDay into the previous one, then and ignore it
				previous.setSecondTo(current.getSecondTo());
				return false;
			}
			previous = current;
			return true;
		}
	}
}

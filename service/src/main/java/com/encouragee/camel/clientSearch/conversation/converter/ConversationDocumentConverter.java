package com.encouragee.camel.clientSearch.conversation.converter;

//import com.encouragee.camel.clientSearch.conversation.Conversation;
import com.encouragee.camel.clientSearch.conversation.index.CalculatorEventVisitor;
import com.encouragee.camel.clientSearch.conversation.index.ConversationCalculator;
import com.encouragee.camel.clientSearch.model.ConversationDocumentList;
import com.encouragee.camel.clientSearch.model.ConversationsPage;
import com.encouragee.model.solr.ConversationDocument;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.zoomint.encourage.common.model.label.Label;
import com.zoomint.encourage.common.model.questionnaire.Questionnaire;
import com.zoomint.encourage.model.conversation.*;
import com.zoomint.encourage.model.conversation.event.*;
import com.zoomint.keycloak.provider.api.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Converter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

import static com.encouragee.camel.clientSearch.conversation.converter.FieldNames.getMetadataFieldName;
import static com.encouragee.camel.clientSearch.conversation.converter.FieldNames.getNumberMetadataFieldName;
import static com.zoomint.encourage.model.conversation.ConversationParticipantType.UNKNOWN;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.hasText;
import static org.springframework.util.StringUtils.trimAllWhitespace;

@Component
@Converter
@Slf4j
public class ConversationDocumentConverter {

	private static final String SCORING_SYSTEM_PERCENTAGE = "PERCENTAGE";
	private static final String SCORING_SYSTEM_POINTS = "POINTS";

	//do not consider spaces and case as difference
	private static final Comparator<String> SUBJECT_COMPARATOR =
			(s1, s2) -> trimAllWhitespace(s1).compareToIgnoreCase(trimAllWhitespace(s2));

	/**
	 * @see #convertToDocument(Conversation)
	 * @param conversations page of conversations to convert
	 * @return list of documents corresponding to the conversations
	 */
	@Converter
	@NotNull
	public ConversationDocumentList convertToDocuments(@NotNull ConversationsPage conversations) {
		return new ConversationDocumentList(
				conversations.getConversations().stream()
						.map(this::convertToDocument)
						.collect(toList()));
	}

	/**
	 * @see #convertToDocument(Conversation)
	 * @param conversations list of conversations to convert
	 * @return list of documents corresponding to the conversations
	 */
	@Converter
	@NotNull
	public ConversationDocumentList convertToDocuments(@NotNull ConversationList conversations) {
		return new ConversationDocumentList(
				conversations.getConversations().stream()
						.map(this::convertToDocument)
						.collect(toList()));
	}

	/**
	 * Converts conversation to Solr search document.
	 * Conversation should already be calculated - see {@link ConversationCalculator}.
	 *
	 * @param conversation conversation to convert
	 * @return search document representation of the conversation
	 */
	@Converter
	@NotNull
	public ConversationDocument convertToDocument(@NotNull Conversation conversation) {
		Assert.notNull(conversation, "conversation must not be null");

		log.trace("Converting conversation to search document: {}", conversation.getConversationId());

		ConversationDocument searchDocument = new ConversationDocument();

		searchDocument.setConversationId(conversation.getConversationId());
		searchDocument.setSchemaVersion(ConversationDocument.CURRENT_SCHEMA_VERSION);
		searchDocument.setPreviousIds(conversation.getPreviousIds());

		// calculate the rest of the fields based on events (should be only active events after merging)
		ConverterEventVisitor visitor = new ConverterEventVisitor();
		conversation.getEvents().stream()
				.sorted(ConversationCalculator.EVENT_ORDERING)
				.forEach(visitor::accept);
		visitor.applyTo(searchDocument);

		log.trace("Converted conversation to search document: {}", conversation.getConversationId());
		return searchDocument;
	}

	private static class ConverterEventVisitor extends CalculatorEventVisitor {
		private final List<String> eventIds = new ArrayList<>();
		private final Set<String> correlationIds = new HashSet<>();
		private Instant latestSegmentStartDateTime = Instant.EPOCH;
		private final Set<String> communicationTypes = new HashSet<>();
		private final Set<String> contactsFrom = new HashSet<>();
		private final Set<String> contactsTo = new HashSet<>();
		private final Set<String> userIds = new HashSet<>();
		private final Set<Long> labelIds = new HashSet<>();
		private final SetMultimap<String, Integer> speechPhrase = LinkedHashMultimap.create();
		private final SetMultimap<String, String> metadata = LinkedHashMultimap.create();
		private final SetMultimap<String, Long> metadataNumber = LinkedHashMultimap.create();
		private final List<String> comment = new ArrayList<>();
		private final Set<String> textSubject = new TreeSet<>(SUBJECT_COMPARATOR);
		private final Set<String> textBody = new HashSet<>();

		private final Set<String> reviewIds = new HashSet<>();
		private final Set<String> reviewQuestionnaireIds = new HashSet<>();
		private final Set<String> reviewerUserIds = new HashSet<>();
		private final Set<String> reviewedUserIds = new HashSet<>();
		private final Set<String> reviewScoringSystems = new HashSet<>();
		private final List<Float> reviewScoresPercentage = new ArrayList<>();
		private final List<Float> reviewScoresPoints = new ArrayList<>();

		private final Set<String> surveyIds = new HashSet<>();
		private final Set<String> surveyQuestionnaireIds = new HashSet<>();
		private final Set<String> surveyedUserIds = new HashSet<>();
		private final Set<String> surveyScoringSystems = new HashSet<>();
		private final List<Float> surveyScoresPercentage = new ArrayList<>();
		private final List<Float> surveyScoresPoints = new ArrayList<>();

		private final Set<String> resourceFlags = new HashSet<>();
		private final List<String> surveyComments = new ArrayList<>();
		private Integer segmentCount = 0;

		public void applyTo(ConversationDocument document) {
			document.setEventIds(eventIds);
			document.setCorrelationIds(correlationIds);
			document.setLatestSegmentStartDateTime(latestSegmentStartDateTime);
			document.setCommunicationTypes(communicationTypes);
			document.setContactsFrom(contactsFrom);
			document.setContactsTo(contactsTo);
			document.setUserUUIDs(userIds);
			document.setLabels(labelIds);
			document.setSpeechPhrase(Multimaps.asMap(LinkedListMultimap.create(speechPhrase)));
			document.setMetadata(Multimaps.asMap(LinkedListMultimap.create(metadata)));
			document.setMetadataNumber(Multimaps.asMap(LinkedListMultimap.create(metadataNumber)));
			document.setComments(comment);
			document.setTextSubject(textSubject);
			document.setTextBody(textBody);

			document.setReviewIds(reviewIds);
			document.setReviewQuestionnaireIds(reviewQuestionnaireIds);
			document.setReviewerUserUUIDs(reviewerUserIds);
			document.setReviewedUserUUIDs(reviewedUserIds);
			document.setReviewScoringSystems(reviewScoringSystems);
			document.setReviewScoresPercentage(reviewScoresPercentage);
			document.setReviewScoresPoints(reviewScoresPoints);

			document.setSurveyIds(surveyIds);
			document.setSurveyQuestionnaireIds(surveyQuestionnaireIds);
			document.setSurveyedUserUUIDs(surveyedUserIds);
			document.setSurveyScoringSystems(surveyScoringSystems);
			document.setSurveyScoresPercentage(surveyScoresPercentage);
			document.setSurveyScoresPoints(surveyScoresPoints);

			document.setResourceFlags(resourceFlags);
			document.setSurveyComments(surveyComments);
			document.setSegmentsCount(segmentCount);
			document.setAgentsCount(userIds.size());

			// fields from parent visitor:
			document.setDirection(direction.name());
			document.setDuration(calculateDuration().toMillis());
			if (start != null) {
				document.setStartDateTime(start);
				document.setDayOfWeek(start.atOffset(ZoneOffset.UTC).getDayOfWeek().getValue());
				document.setSecondOfDay(start.atOffset(ZoneOffset.UTC).toLocalTime().toSecondOfDay());
			}
		}

		@Override
		public void visitAll(@NotNull ConversationEvent event) {
			super.visitAll(event);
			event.streamAllParticipants().forEach(this::handleParticipant);
			eventIds.add(event.getEventId());
			correlationIds.addAll(event.getCorrelationIds());
		}

		@Override
		public void visitCommunication(@NotNull MediaEvent event) {
			super.visitCommunication(event);
			communicationTypes.add(event.getCommunicationType().name());
			handleLatestSegmentStart(event.getStart());
			handleResource(event.getResource());
		}

		@Override
		public void visitTextMessage(@NotNull TextMessageEvent event) {
			super.visitTextMessage(event);
			segmentCount++;
			if (hasText(event.getTopic())) {
				textSubject.add(event.getTopic());
			}
			if (hasText(event.getTextBody())) {
				textBody.add(event.getTextBody());
			}
		}

		@Override
		public void visitKeyValueData(@NotNull KeyValueDataEvent event) {
			super.visitKeyValueData(event);
			if (event.getData() != null) {
				event.getData().entrySet().stream().distinct().forEach(this::handleMetadata);
			}
		}

		@Override
		public void visit(@NotNull StartedCallEvent event) {
			super.visit(event);
			segmentCount++;
			handleParticipantContacts(event.getParticipant(), contactsFrom);
		}

		@Override
		public void visit(@NotNull JoinedCallEvent event) {
			super.visit(event);
			handleParticipantContacts(event.getParticipant(), contactsTo);
		}

		@Override
		public void visit(@NotNull StartedVideoEvent event) {
			super.visit(event);
			segmentCount++;
			handleParticipantContacts(event.getParticipant(), contactsFrom);
		}

		@Override
		public void visit(@NotNull ChatEvent event) {
			super.visit(event);
			handleParticipantContacts(event.getParticipant(), contactsFrom);
			event.getParticipantsTo().forEach(participant -> handleParticipantContacts(participant, contactsTo));
		}

		@Override
		public void visit(@NotNull EmailEvent event) {
			super.visit(event);
			handleParticipantContacts(event.getParticipant(), contactsFrom);
			event.getParticipantsTo().forEach(participant -> handleParticipantContacts(participant, contactsTo));
			event.getParticipantsCC().forEach(participant -> handleParticipantContacts(participant, contactsTo));
		}

		@Override
		public void visit(@NotNull PhraseOccurrenceEvent event) {
			super.visit(event);
			if (event.getStart() != null && event.getPhrase() != null && event.getPhraseProbability() != null) {
				ConversationParticipantType participantType = Optional.ofNullable(event.getParticipant())
						.map(ConversationParticipant::getType)
						.orElse(UNKNOWN);
				String fieldName = FieldNames.getPhraseFieldName(event.getPhrase().getSpeechPhraseId(), participantType);
				speechPhrase.put(fieldName, event.getPhraseProbability());
			}
		}

		@Override
		public void visit(@NotNull TagEvent event) {
			super.visit(event);
			event.getLabels().stream().map(Label::getLabelId).forEach(labelIds::add);
			if (hasText(event.getComment())) {
				comment.add(event.getComment());
			}
		}

		@Override
		public void visit(@NotNull ReviewEvent event) {
			super.visit(event);
			Optional.ofNullable(event.getReviewer()).map(User::getUserId).ifPresent(reviewerUserIds::add);
			Optional.ofNullable(event.getReviewee()).map(User::getUserId).ifPresent(reviewedUserIds::add);
			if (hasText(event.getReviewId())) {
				reviewIds.add(event.getReviewId());
			}
			if (hasText(event.getQuestionnaireId())) {
				reviewQuestionnaireIds.add(event.getQuestionnaireId());
			}
			handleScore(event.getQuestionnaire(), event.getScore(), reviewScoringSystems, reviewScoresPercentage, reviewScoresPoints);
		}

		@Override
		public void visit(@NotNull SurveyEvent event) {
			super.visit(event);
			Optional.ofNullable(event.getReviewee()).map(User::getUserId).ifPresent(surveyedUserIds::add);
			if (hasText(event.getSurveyId())) {
				surveyIds.add(event.getSurveyId());
			}
			if (hasText(event.getQuestionnaireId())) {
				surveyQuestionnaireIds.add(event.getQuestionnaireId());
			}
			if (hasText(event.getComment())) {
				surveyComments.add(event.getComment());
			}
			handleScore(event.getQuestionnaire(), event.getScore(), surveyScoringSystems, surveyScoresPercentage, surveyScoresPoints);
		}

		private void handleScore(Questionnaire questionnaire, Float score,
				Set<String> scoringSystems, List<Float> scoresPercentage, List<Float> scoresPoints) {

			if (questionnaire == null || !hasText(questionnaire.getScoringSystem())) {
				return;
			}
			String scoringSystem = questionnaire.getScoringSystem();
			scoringSystems.add(scoringSystem);
			if (score != null && SCORING_SYSTEM_PERCENTAGE.equalsIgnoreCase(scoringSystem)) {
				scoresPercentage.add(score);
			} else if (score != null && SCORING_SYSTEM_POINTS.equalsIgnoreCase(scoringSystem)) {
				scoresPoints.add(score);
			}
		}

		private void handleResource(ConversationResource resource) {
			if (resource == null) {
				return;
			}
			Set<String> flags = new HashSet<>(0);
			while (resource != null) {
				flags.addAll(resource.getFlags());
				resource = resource.getParent();
			}
			if (!flags.isEmpty()) {
				resourceFlags.addAll(flags);
			} else {
				// if this resource's hierarchy has no flags, default to no problem
				resourceFlags.add(ConversationResource.FLAG_NO_PROBLEM);
			}
		}

		private void handleParticipant(ConversationParticipant participant) {
			if (participant == null) {
				return;
			}
			Optional.ofNullable(participant.getUser())
					.map(User::getUserId)
					.ifPresent(userIds::add);
		}

		private void handleParticipantContacts(ConversationParticipant participant, Set<String> contacts) {
			if (participant == null) {
				return;
			}
			if (hasText(participant.getPhoneNumber())) {
				contacts.add(participant.getPhoneNumber());
			}
			if (hasText(participant.getAddress())) {
				contacts.add(participant.getAddress());
			}
		}

		private void handleLatestSegmentStart(ZonedDateTime eventStart) {
			Optional.ofNullable(eventStart)
					.map(ZonedDateTime::toInstant)
					// update latestSegmentStartDateTime if later
					.filter(candidate -> latestSegmentStartDateTime.isBefore(candidate))
					.ifPresent(candidate -> latestSegmentStartDateTime = candidate);
		}

		private void handleMetadata(Map.Entry<String, String> entry) {
			metadata.put(getMetadataFieldName(entry.getKey()), entry.getValue().toLowerCase(Locale.ROOT));
			if (entry.getValue().length() <= 15
					&& ConversationDocument.NUMBER_PATTERN.matcher(entry.getValue()).matches()) {
				metadataNumber.put(getNumberMetadataFieldName(entry.getKey()), Long.valueOf(entry.getValue()));
			}
		}
	}
}

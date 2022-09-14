package com.encouragee.camel.clientSearch.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.zoomint.encourage.common.exceptions.database.ConflictEntityException;
import com.zoomint.encourage.common.model.label.Label;
import com.zoomint.encourage.common.model.label.LabelLookup;
import com.zoomint.encourage.common.model.questionnaire.Questionnaire;
import com.zoomint.encourage.common.model.search.SearchTemplateList;
import com.zoomint.encourage.common.model.speech.SpeechPhrase;
import com.zoomint.encourage.common.model.speech.SpeechPhraseLookup;
import com.zoomint.encourage.model.conversation.ConversationEvent;
import com.zoomint.encourage.model.conversation.ConversationEventVisitorBase;
import com.zoomint.encourage.model.conversation.ConversationParticipant;
import com.zoomint.encourage.model.conversation.event.PhraseOccurrenceEvent;
import com.zoomint.encourage.model.conversation.event.ReviewEvent;
import com.zoomint.encourage.model.conversation.event.SurveyEvent;
import com.zoomint.encourage.model.conversation.event.TagEvent;
import com.zoomint.encourage.model.search.ClientConversationSearch;
import com.zoomint.keycloak.provider.api.dto.Group;
import com.zoomint.keycloak.provider.api.dto.User;
import com.zoomint.keycloak.provider.api.dto.UserLookup;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.google.common.collect.Maps.uniqueIndex;
import static com.google.common.collect.Multimaps.index;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toSet;

/**
 * Holder for enriching entities:
 * allows collecting entity IDs necessary to perform a bulk lookup,
 * then also allows assigning looked up entities based on originally IDs.
 */
@Slf4j
@SuppressWarnings("PMD.ExcessivePublicCount")
public class ConversationEntities {
	private Map<String, User> usersById = emptyMap();
	private Multimap<String, Group> groupsByParentUUID = ImmutableMultimap.of();
	private Map<Long, Label> labelsById = emptyMap();
	private Map<Long, SpeechPhrase> phrasesById = emptyMap();
	private Map<String, Questionnaire> questionnairesByExternalId = emptyMap();

	private Set<String> userUUIDs = new HashSet<>();
	private Set<String> parentGroupUUIDs = new HashSet<>();
	private Set<Long> labelIds = new HashSet<>();
	private Set<Long> speechPhraseIds = new HashSet<>();
	private Set<String> questionnaireExternalIds = new HashSet<>();

	public ConversationEntities forEvents(Stream<ConversationEvent> events) {
		events.forEach(new LookupVisitor()::accept);
		return this;
	}

	public ConversationEntities enrichEvents(Stream<ConversationEvent> events) {
		events.forEach(new EnrichVisitor()::accept);
		return this;
	}

	public ConversationEntities forSearch(ClientConversationSearch search) {
		parentGroupUUIDs.addAll(search.getGroupUUIDs());
		search.getLabels().forEach(searchable -> labelIds.add(searchable.getLabelId()));
		return this;
	}

	public ConversationEntities enrichSearch(ClientConversationSearch search) {
		// add child groups to groupIds
		search.setGroupUUIDs(withChildGroups(search.getGroupUUIDs()));
		// add fully loaded labels
		search.getLabels().forEach(searchableLabel ->
				searchableLabel.setLabel(Optional.ofNullable(labelsById.get(searchableLabel.getLabelId()))
						.orElseThrow(() -> new ConflictEntityException(
								"Label doesn't exist for LabelId: " + searchableLabel.getLabelId()))));
		return this;
	}

	public ConversationEntities forSearchTemplates(SearchTemplateList templates) {
		templates.getSearchTemplates().forEach(template -> forSearch(template.getConversationSearch()));
		return this;
	}

	public ConversationEntities enrichSearchTemplates(SearchTemplateList templates) {
		templates.getSearchTemplates().forEach(template -> enrichSearch(template.getConversationSearch()));
		return this;
	}

	public ConversationEntities forSearchPermissions(ConversationSearchPermissions permissions) {
		parentGroupUUIDs.addAll(permissions.getAllowedGroupUUIDs());
		return this;
	}

	public ConversationEntities enrichSearchPermissions(ConversationSearchPermissions permissions) {
		// add child groups to groupIds
		permissions.setAllowedGroupUUIDs(withChildGroupUUIDs(permissions.getAllowedGroupUUIDs()));
		return this;
	}

	public UserLookup getUserLookup() {
		return UserLookup.builder().ids(userUUIDs).build();
	}

	public Set<String> getParentGroupUUIDs() {
		return parentGroupUUIDs;
	}

	public LabelLookup getLabelLookup() {
		return LabelLookup.builder().labelIds(labelIds).build();
	}

	public SpeechPhraseLookup getPhraseLookup() {
		return SpeechPhraseLookup.builder().speechPhraseIds(speechPhraseIds).build();
	}

	public Set<String> getQuestionnaireExternalIds() {
		return questionnaireExternalIds;
	}

	public ConversationEntities setUsers(List<User> users) {
		this.usersById = uniqueIndex(users, User::getUserId);
		return this;
	}

	public ConversationEntities setGroups(Set<Group> groups) {
		groupsByParentUUID = index(
				groups.stream()
						.filter(group -> group.getGroupParentId() != null) // skip root group
						.iterator(),
				Group::getGroupParentId);
		return this;
	}

	public ConversationEntities setLabels(List<Label> labels) {
		this.labelsById = uniqueIndex(labels, Label::getLabelId);
		return this;
	}


	public ConversationEntities setPhrases(List<SpeechPhrase> phrases) {
		this.phrasesById = uniqueIndex(phrases, SpeechPhrase::getSpeechPhraseId);
		return this;
	}

	public ConversationEntities setQuestionnaires(Map<String, Questionnaire> questionnaires) {
		this.questionnairesByExternalId = ImmutableMap.copyOf(questionnaires);
		return this;
	}

	private Set<String> withChildGroups(Set<String> parentGroupUUIDs) {
		Set<String> groupUUIDs = new HashSet<>(parentGroupUUIDs);
		LinkedList<String> groupQueue = new LinkedList<>(groupUUIDs);
		while (!groupQueue.isEmpty()) {
			String groupUUID = groupQueue.remove();
			groupsByParentUUID.get(groupUUID).forEach(child -> {
				if (groupUUIDs.add(child.getGroupId())) {
					groupQueue.add(child.getGroupId());
				}
			});
		}
		return groupUUIDs;
	}

	private Set<String> withChildGroupUUIDs(Set<String> parentGroupUUIds) {
		Set<String> groupUUIds = new HashSet<>(parentGroupUUIds);
		LinkedList<String> groupQueue = new LinkedList<>(groupUUIds);
		while (!groupQueue.isEmpty()) {
			String groupId = groupQueue.remove();
			groupsByParentUUID.get(groupId).forEach(child -> {
				if (groupUUIds.add(child.getGroupId())) {
					groupQueue.add(child.getGroupId());
				}
			});
		}
		return groupUUIds;
	}

	/**
	 * Visitor that accumulates IDs of entities to lookup.
	 */
	public class LookupVisitor extends ConversationEventVisitorBase {

		private void handleUser(User user) {
			if (user != null && user.getUserId() != null) {
				userUUIDs.add(user.getUserId());
			}
		}

		private void handleQuestionnaireId(String questionnaireId) {
			if (questionnaireId != null) {
				questionnaireExternalIds.add(questionnaireId);
			}
		}

		@Override
		public void visitAll(@NotNull ConversationEvent event) {
			handleUser(event.getCreatedBy());
			event.streamAllParticipants()
					.map(ConversationParticipant::getUser)
					.forEach(this::handleUser);
		}

		@Override
		public void visit(@NotNull TagEvent event) {
			super.visit(event);
			event.getLabels().stream()
					.filter(Objects::nonNull)
					.map(Label::getLabelId)
					.filter(Objects::nonNull)
					.forEach(labelIds::add);
		}

		@Override
		public void visit(@NotNull PhraseOccurrenceEvent event) {
			super.visit(event);
			SpeechPhrase phrase = event.getPhrase();
			if (phrase != null && phrase.getSpeechPhraseId() != null) {
				speechPhraseIds.add(phrase.getSpeechPhraseId());
			}
		}

		@Override
		public void visit(@NotNull ReviewEvent event) {
			super.visit(event);
			handleUser(event.getReviewee());
			handleUser(event.getReviewer());
			handleQuestionnaireId(event.getQuestionnaireId());
		}

		@Override
		public void visit(@NotNull SurveyEvent event) {
			super.visit(event);
			handleUser(event.getReviewee());
			handleQuestionnaireId(event.getQuestionnaireId());
		}
	}

	/**
	 * Visitor that enriches events using the accumulated enriched entities.
	 */
	public class EnrichVisitor extends ConversationEventVisitorBase {

		private Optional<User> findUser(User user) {
			return Optional.ofNullable(user)
					.map(User::getUserId)
					.map(usersById::get);
		}

		private Optional<Label> findLabel(Label label) {
			return Optional.ofNullable(label)
					.map(Label::getLabelId)
					.map(labelsById::get);
		}

		private Optional<SpeechPhrase> findPhrase(SpeechPhrase phrase) {
			return Optional.ofNullable(phrase)
					.map(SpeechPhrase::getId)
					.map(phrasesById::get);
		}

		private void enrichQuestionnaire(String questionnaireId, Consumer<Questionnaire> setter) {
			Optional.ofNullable(questionnaireId)
					.map(questionnairesByExternalId::get)
					.ifPresent(setter);
		}

		@Override
		public void visitAll(@NotNull ConversationEvent event) {
			findUser(event.getCreatedBy()).ifPresent(event::setCreatedBy);
			event.streamAllParticipants().forEach(participant -> findUser(participant.getUser()).ifPresent(participant::setUser));
		}

		@Override
		public void visit(@NotNull TagEvent event) {
			super.visit(event);
			event.setLabels(event.getLabels().stream()
					.map(label -> findLabel(label).orElse(label))
					.collect(toSet()));
		}

		@Override
		public void visit(@NotNull PhraseOccurrenceEvent event) {
			super.visit(event);
			findPhrase(event.getPhrase()).ifPresent(event::setPhrase);
		}

		@Override
		public void visit(@NotNull ReviewEvent event) {
			super.visit(event);
			findUser(event.getReviewee()).ifPresent(event::setReviewee);
			findUser(event.getReviewer()).ifPresent(event::setReviewer);
			enrichQuestionnaire(event.getQuestionnaireId(), event::setQuestionnaire);
		}

		@Override
		public void visit(@NotNull SurveyEvent event) {
			super.visit(event);
			findUser(event.getReviewee()).ifPresent(event::setReviewee);
			enrichQuestionnaire(event.getQuestionnaireId(), event::setQuestionnaire);
		}
	}
}

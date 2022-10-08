package com.encouragee.camel.clientSearch.conversation.converter;

import com.encouragee.camel.clientSearch.model.ConversationUpdate;
import com.encouragee.model.solr.ConversationDocument;
import com.zoomint.encourage.common.model.label.Label;
import com.zoomint.encourage.common.model.label.LabelLookup;
import com.zoomint.encourage.model.conversation.ConversationEvent;
import com.zoomint.encourage.model.conversation.ConversationParticipant;
import com.zoomint.encourage.model.conversation.event.EventLookup;
import com.zoomint.encourage.model.conversation.event.TagEvent;
import com.zoomint.keycloak.provider.api.dto.User;
import com.zoomint.keycloak.provider.api.dto.UserLookup;
import org.apache.camel.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

/**
 * Converts various entities/lists that reference other entities into a big list of all the referenced entities.
 * This is usually necessary to perform a bulk lookup of referenced entities.
 */
@Component
@Converter
public class LookupsConverter {

	@Converter
	public LabelLookup lookupLabelsInTagEvent(TagEvent tagEvent) {
		return LabelLookup.builder()
				.labelIds(tagEvent.getLabels().stream()
						.map(Label::getLabelId)
						.collect(toSet()))
				.build();
	}

	@Converter
	public EventLookup lookupEventsInSearchResults(Page<ConversationDocument> results) {
		return EventLookup.builder()
				.eventIds(results.getContent().stream()
						.flatMap(document -> document.getEventIds().stream())
						.collect(toSet()))
				.build();
	}

	@Converter
	public EventLookup lookupEventsInConversationUpdate(ConversationUpdate update) {
		return EventLookup.builder()
				.eventIds(Stream
						.concat(
								update.getConversations().stream()
										.flatMap(conversation -> conversation.getEvents().stream())
										.map(ConversationEvent::getEventId),
								update.getDocumentById().values().stream()
										.flatMap(document -> document.getEventIds().stream())
						)
						.collect(toSet()))
				.build();
	}

	@Converter
	public UserLookup getUserLookup(ConversationUpdate update) {
		return UserLookup.builder()
				.ids(update.getConversations().stream()
						.flatMap(conversation -> conversation.getEvents().stream())
						.flatMap(ConversationEvent::streamAllParticipants)
						.map(ConversationParticipant::getUser)
						.filter(Objects::nonNull)
						.map(User::getUserId)
						.filter(Objects::nonNull)
						.collect(toSet()))
				.build();
	}
}

package com.encouragee.camel.clientSearch.conversation.converter;

import com.encouragee.camel.clientSearch.model.ConversationUpdate;
import com.zoomint.encourage.model.conversation.Conversation;
import com.zoomint.encourage.model.conversation.ConversationList;
import com.zoomint.encourage.model.conversation.event.EventList;
import org.apache.camel.Converter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Converter
@Component
@Profile("solr")
public class ConversationUpdateConverter {

	@Converter
	public ConversationUpdate fromConversations(ConversationList list) {
		return new ConversationUpdate(list.getConversations());
	}

	@Converter
	public ConversationUpdate fromConversation(Conversation conversation) {
		return new ConversationUpdate(singletonList(conversation));
	}

	@Converter
	public EventList toEventList(ConversationUpdate update) {
		return new EventList(update.getConversations().stream()
				.flatMap(conversation -> conversation.getEvents().stream())
				.collect(toList()));
	}
}

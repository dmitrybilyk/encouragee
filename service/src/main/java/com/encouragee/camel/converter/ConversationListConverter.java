package com.encouragee.camel.converter;

import com.zoomint.encourage.model.conversation.Conversation;
import com.zoomint.encourage.model.conversation.ConversationList;
import org.apache.camel.Converter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Converter
public class ConversationListConverter {

	@Converter
	@NotNull
	public ConversationList fromConversation(List<Conversation> conversations) {
		return new ConversationList(conversations);
	}

}

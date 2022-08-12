package com.encouragee.camel.converter;

import com.zoomint.encourage.model.conversation.Conversation;
import com.zoomint.encourage.model.conversation.event.EventList;
import org.apache.camel.Converter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Converter
public class EventListConverter {

	@Converter
	@NotNull
	public EventList fromConversation(List<Conversation> conversations) {
		return new EventList(conversations.stream().flatMap(conv -> conv.getEvents().stream()).collect(Collectors.toList()));
	}

}

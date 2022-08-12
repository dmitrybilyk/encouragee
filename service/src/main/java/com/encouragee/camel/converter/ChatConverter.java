package com.encouragee.camel.converter;

//import com.zoomint.encourage.integrations.api.ApiProperties;
//import com.zoomint.encourage.integrations.api.model.Chat;
import com.encouragee.ApiProperties;
import com.encouragee.camel.model.Chat;
//import com.zoomint.encourage.integrations.api.model.Chat.ChatParticipant;
import com.zoomint.encourage.model.conversation.Conversation;
import com.zoomint.encourage.model.conversation.ConversationParticipant;
import com.zoomint.encourage.model.conversation.ConversationResource;
import com.zoomint.encourage.model.conversation.event.ChatEvent;
import com.zoomint.encourage.model.search.Direction;
import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.hasText;

@Component
@Converter
public class ChatConverter {
	private static final UriComponents MESSAGE_ID_URI =
			UriComponentsBuilder.fromUriString("{systemType}:messageid:{messageId}").build();

	private final ApiProperties properties;

	public ChatConverter(ApiProperties properties) {
		this.properties = properties;
	}

	@Converter
	@NotNull
	public Conversation fromChat(@NotNull Chat chat, @NotNull Exchange exchange) {
		return fromChat(chat, exchange.getProperty(ConversationEntities.ENTITIES_PROPERTY, ConversationEntities.class));
	}

	@NotNull
	Conversation fromChat(@NotNull Chat chat, @NotNull ConversationEntities entities) {
		return Conversation.builder()
				.event(createChatEvent(chat, entities))
				.build();
	}

	private ChatEvent createChatEvent(@NotNull Chat chat, @NotNull ConversationEntities entities) {
		return ChatEvent.builder()
				.eventId(getMessageId(chat))
				.start(chat.getCreated())
				.correlationId(getChatId(chat))
				.direction(Direction.UNKNOWN)
				.resource(getResource(chat))
				.chatName(chat.getChatName())
				.text(chat.getText())
				.participant(fromChatParticipant(chat.getFrom(), entities))
				.participantsTo(fromChatParticipants(chat.getTo(), entities))
				.attachments(firstNonNull(chat.getAttachments(), emptySet()))
				.data(firstNonNull(chat.getData(), emptyMap()))
				.build();
	}

	@NotNull
	private ConversationResource getResource(Chat chat) {
		return new ConversationResource(MESSAGE_ID_URI
				.expand(
						properties.getChat().getExternalSystemType(),
						chat.getMessageId())
				.toUri());
	}

	private ConversationParticipant fromChatParticipant(@Nullable Chat.ChatParticipant participant, @NotNull ConversationEntities entities) {
		if (participant == null || !hasText(participant.getEmail())) {
			return null;
		}
		return ConversationParticipant.builder()
				.address(participant.getEmail())
				.user(entities.getUser(participant.getEmail()))
				.build();
	}

	private List<ConversationParticipant> fromChatParticipants(@NotNull List<Chat.ChatParticipant> participants, @NotNull ConversationEntities entities) {
		return participants.stream()
				.map(participant -> fromChatParticipant(participant, entities))
				.filter(Objects::nonNull)
				.collect(toList());
	}

	private String getChatId(Chat chat) {
		return properties.getChat().getExternalSystemType()
				+ ":chatid:" + chat.getChatId();
	}

	private String getMessageId(Chat chat) {
		return properties.getChat().getExternalSystemType()
				+ ":messageid:" + chat.getMessageId();
	}
}

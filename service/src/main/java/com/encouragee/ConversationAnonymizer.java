package com.encouragee;

import com.zoomint.encourage.common.model.settings.Field;
import com.zoomint.encourage.model.conversation.ConversationEvent;
import com.zoomint.encourage.model.conversation.ConversationEventVisitorBase;
import com.zoomint.encourage.model.conversation.ConversationParticipantType;
import com.zoomint.encourage.model.conversation.ConversationResource;
import com.zoomint.encourage.model.conversation.event.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.zoomint.encourage.model.EncourageConstants.*;
import static com.zoomint.encourage.model.conversation.ConversationParticipantType.CUSTOMER;
import static com.zoomint.encourage.model.conversation.ConversationResource.*;
import static org.springframework.util.StringUtils.hasText;

public class ConversationAnonymizer extends ConversationEventVisitorBase {
	private final AnonymizeEvent anonymizeEvent;
	private final Set<ConversationEvent> modifiedEvents;

	private final boolean anonText;
	private final boolean anonAddress;
	private final boolean anonPhone;
	private final boolean anonMedia;

	public ConversationAnonymizer(AnonymizeEvent anonymizeEvent, Set<ConversationEvent> modifiedEvents) {
		this.anonymizeEvent = anonymizeEvent;
		this.modifiedEvents = modifiedEvents;
		anonText = anonymizeEvent.getFields().contains(Field.SUBJECT_BODY);
		anonAddress = anonymizeEvent.getFields().contains(Field.PARTICIPANT_ADDRESS);
		anonPhone = anonymizeEvent.getFields().contains(Field.PARTICIPANT_PHONE);
		anonMedia = anonymizeEvent.getFields().contains(Field.MEDIA_FILES);
	}

	@Override
	public void visitAll(@NotNull ConversationEvent event) {
		super.visitAll(event);
		if (anonAddress || anonPhone) {
			// anonymize participants
			event.streamAllParticipants()
					.filter(participant -> participant.getType() != ConversationParticipantType.AGENT)
					.forEach(participant -> {
						if (anonAddress) {
							anonymizeField(event, participant::getAddress, participant::setAddress, FIELD_ANONYMIZED_ADDRESS);
						}
						if (anonPhone) {
							anonymizeField(event, participant::getPhoneNumber, participant::setPhoneNumber, FIELD_ANONYMIZED_PHONE);
						}
					});
		}
		if (anonMedia && isEventWithMediaFile(event)) {
			addFlag(event, FLAG_DELETE_REQUESTED);
			removeFlagDeep(event, FLAG_PROTECTED); // unprotect whole couple
		}
	}

	private boolean isEventWithMediaFile(@NotNull ConversationEvent event) {
		return event instanceof StartedCallEvent || event instanceof JoinedCallEvent || event instanceof LeftCallEvent
				|| event instanceof StartedScreenEvent || event instanceof EndedScreenEvent || event instanceof StartedVideoEvent;
	}

	@Override
	public void visitKeyValueData(@NotNull KeyValueDataEvent event) {
		super.visitKeyValueData(event);
		for (Map.Entry<String, String> entry : event.getData().entrySet()) {
			if (anonymizeEvent.getMetadataKeys().contains(entry.getKey())) {
				anonymizeField(event, entry::getValue, entry::setValue, FIELD_ANONYMIZED_METADATA);
			}
		}
	}

	@Override
	public void visitTextMessage(@NotNull TextMessageEvent event) {
		super.visitTextMessage(event);
		if (anonText) {
			anonymizeField(event, event::getTextBody, event::setTextBody, FIELD_ANONYMIZED_TEXT);
			anonymizeField(event, event::getTopic, event::setTopic, FIELD_ANONYMIZED_TEXT);
			addFlag(event, FLAG_DELETED);
		}
	}

	@Override
	public void visit(@NotNull StartedCallEvent event) {
		super.visit(event);
		if (anonPhone && event.getParticipant() != null && event.getParticipant().getType() != CUSTOMER) {
			// if the calling party was not customer, defensively anonymize called number
			anonymizeField(event, event::getCalledNumber, event::setCalledNumber, FIELD_ANONYMIZED_PHONE);
		}
	}

	private void addFlag(ConversationEvent event, String flag) {
		ConversationResource resource = event.getResource();
		if (!resource.getFlags().contains(flag)) {
			resource.setFlags(new HashSet<>(resource.getFlags()));
			resource.getFlags().add(flag);
			modifiedEvents.add(event);
		}
	}

	private void removeFlagDeep(ConversationEvent event, String flag) {
		ConversationResource resource = event.getResource();
		while (resource != null) {
			if (resource.getFlags().contains(flag)) {
				resource.setFlags(new HashSet<>(resource.getFlags()));
				resource.getFlags().remove(flag);
				modifiedEvents.add(event);
			}
			resource = resource.getParent();
		}
	}

	private void anonymizeField(ConversationEvent event, Supplier<String> getter, Consumer<String> setter, String anonValue) {
		String originalValue = getter.get();
		if (hasText(originalValue) && !originalValue.equals(anonValue)) {
			setter.accept(anonValue);
			modifiedEvents.add(event);
		}
	}

}

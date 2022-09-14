package com.encouragee.camel.clientSearch.model;

import com.zoomint.encourage.model.conversation.ConversationEvent;
import lombok.*;
import net.jcip.annotations.Immutable;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Filter for choosing which events will be returned in conversations as part of the conversation search results.
 */
@Immutable
@Value
@Builder
@AllArgsConstructor
public class EventFilter implements Predicate<ConversationEvent> {

	/**
	 * Event types to be included in the result
	 */
	@NonNull
	@Singular
	Set<String> eventTypes;

	/**
	 * Controls whether to make visible events created by the specified user, regardless of the type.
	 */
	String createdBy;

	/**
	 * @param event event to apply this set of filters to
	 * @return true to keep the specified event, false to remove the specified event
	 */
	@Override
	public boolean test(ConversationEvent event) {
		return eventTypes.contains(event.getType()) || (createdBy != null && isOwnEvent(createdBy, event));
	}

	private boolean isOwnEvent(String userId, ConversationEvent event) {
		return event.getCreatedBy() != null && event.getCreatedBy().getUserId() != null && userId.equals(event.getCreatedBy().getUserId());
	}

	public static class EventFilterBuilder {}
}

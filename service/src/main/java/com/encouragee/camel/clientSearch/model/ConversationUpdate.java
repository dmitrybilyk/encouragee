package com.encouragee.camel.clientSearch.model;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.zoomint.encourage.model.conversation.Conversation;
import com.zoomint.encourage.model.conversation.ConversationEvent;
import com.zoomint.encourage.model.conversation.event.EventList;
import com.zoomint.keycloak.provider.api.dto.User;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNullElseGet;

/**
 * Contains everything needed for updating one or more conversations.
 */
@Getter
@Setter
public class ConversationUpdate {

	/**
	 * The actual update(s) in the form of conversations, which could be just additional events or previousIds.
	 */
	private final List<Conversation> conversations;

	/**
	 * Whether the update should fail if some of the documents are missing (would be created).
	 */
	private boolean failOnMissing;

	/**
	 * Modifications to be applied to fully loaded conversations.
	 */
	private Consumer<Conversation> modifications;

	/**
	 * Any events that were changed by this update.
	 */
	private final Set<ConversationEvent> changedEvents = new HashSet<>();

	/**
	 * Any events that were deleted by this update.
	 */
	private final Set<ConversationEvent> deletedEvents = new HashSet<>();

	/**
	 * Affected or related documents by eventId.
	 */
	private final Map<String, ConversationDocument> documentById = new HashMap<>();

	/**
	 * Affected or related events by eventId.
	 */
	private final Map<String, ConversationEvent> eventById = new HashMap<>();

	/**
	 * Which groups each user belongs to as a member.
	 */
	private final SetMultimap<String, String> groupIdByMemberUserId = HashMultimap.create();

	public ConversationUpdate(@NonNull List<Conversation> conversations) {
		this.conversations = conversations;
	}

	public ConversationUpdate addEvents(EventList list) {
		if (list != null) {
			list.getEvents().forEach(event -> eventById.put(event.getEventId(), event));
		}
		return this;
	}

	public ConversationUpdate addMemberUserRoles(@Nullable List<User> users) {
		for (User user : requireNonNullElseGet(users, List::<User>of)) {
			for (String groupId : requireNonNullElseGet(user.getGroups(), List::<String>of)) {
				groupIdByMemberUserId.put(user.getUserId(), groupId);
			}
		}
		return this;
	}

	public Collection<String> getGroupIdsForMemberUserIds(Collection<String> userIds) {
		return userIds.stream()
				.flatMap(userId -> groupIdByMemberUserId.get(userId).stream())
				.collect(Collectors.toSet());
	}
}

package com.encouragee.camel.clientSearch.conversation.converter;

import com.encouragee.camel.clientSearch.model.ConversationSearchPermissions;
import com.encouragee.camel.clientSearch.model.EventFilter;
import com.google.common.base.Enums;
import com.zoomint.encourage.common.exceptions.security.AuthorizationException;
import com.zoomint.encourage.common.model.user.Activity;
import com.zoomint.keycloak.provider.api.dto.Role;
import com.zoomint.keycloak.provider.api.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

import static com.zoomint.encourage.common.model.user.Activity.Activities.*;
import static com.zoomint.encourage.model.conversation.ConversationEvent.*;

@Component
@Slf4j
@Profile("solr")
public class ConversationRestriction {

	private static final Set<String> RELEVANT_ACTIVITIES = Set.of(
			INTERACTIONS_FULL_VIEW.name(), INTERACTIONS_GROUP_VIEW.name(), INTERACTIONS_AGENT_VIEW.name(),
			INTERACTION_REVIEWS_VIEW.name(), INTERACTION_TAGS_VIEW.name()
	);

	public static final Set<String> UNRESTRICTED_EVENT_TYPES = Set.of(
			STARTED_CALL, JOINED_CALL, LEFT_CALL, STARTED_SCREEN, ENDED_SCREEN, EMAIL, CHAT, METADATA,
			PHRASE_OCCURRENCE, DELETE, ANONYMIZE
	);

	public ConversationSearchPermissions getConversationSearchPermissions(User currentUser) {
		if (currentUser == null || currentUser.getUserId() == null) {
			throw new AuthorizationException(); // unknown user - was not authenticated properly
		}

		ConversationSearchPermissions.ConversationSearchPermissionsBuilder filter = ConversationSearchPermissions.builder();
		EventFilter.EventFilterBuilder eventFilter = EventFilter.builder()
				.createdBy(currentUser.getUserId())
				.eventTypes(new HashSet<>(UNRESTRICTED_EVENT_TYPES));

		BiConsumer<String, Set<String>> activityHandler = (String role, Set<String> groupsIds) -> {
			Activity.Activities activity = Enums.getIfPresent(Activity.Activities.class, role).orNull();
			if (activity == null) {
				log.warn("Unknown role: '" + role + "'");
				return;
			}

			switch (activity) {
				case INTERACTIONS_FULL_VIEW:
					filter.allowAll(true);
					break;
				case INTERACTIONS_GROUP_VIEW:
					if (CollectionUtils.isEmpty(groupsIds)) {
						log.error(INTERACTIONS_GROUP_VIEW.name() + " is assigned without any group for username = {} !", currentUser.getUsername());
						break;
					}
					groupsIds.forEach(filter::allowedGroupUUID);
					break;
				case INTERACTIONS_AGENT_VIEW:
					filter.allowedUserId(currentUser.getUserId());
					break;
				case INTERACTION_REVIEWS_VIEW:
					eventFilter.eventType(REVIEW);
					eventFilter.eventType(SURVEY);
					break;
				case INTERACTION_TAGS_VIEW:
					eventFilter.eventType(TAG);
					break;
				default:
					// ignore other permissions
			}
		};

		handleUserActivities(currentUser, activityHandler);

		return filter
				.eventFilter(eventFilter.build())
				.build();
	}

	private void handleUserActivities(User user, BiConsumer<String, Set<String>> activityHandler) {
		user.getRoles().stream()
				.flatMap(role -> getAllActivities(role, new HashSet<>()).stream())
				.forEach(roleName -> {
					if (RELEVANT_ACTIVITIES.contains(roleName)) {
						activityHandler.accept(roleName, user.getUsersTeams());
					}
				});
	}

	private Set<String> getAllActivities(Role role, Set<String> collectedActivities) {
		collectedActivities.add(role.getName());
		if (CollectionUtils.isEmpty(role.getComposites())) {
			return collectedActivities;
		}
		role.getComposites().forEach(one -> getAllActivities(one, collectedActivities));

		return collectedActivities;
	}
}

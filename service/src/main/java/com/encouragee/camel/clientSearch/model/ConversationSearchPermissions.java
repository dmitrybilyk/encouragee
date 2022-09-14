package com.encouragee.camel.clientSearch.model;

import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ConversationSearchPermissions {

	private boolean allowAll;

	@Singular
	@NonNull
	private Set<String> allowedGroupUUIDs;

	@Nullable
	private String allowedUserId;

	@Nullable
	private EventFilter eventFilter;

	public static class ConversationSearchPermissionsBuilder {}
}

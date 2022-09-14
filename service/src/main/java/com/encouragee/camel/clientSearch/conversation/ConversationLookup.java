package com.encouragee.camel.clientSearch.conversation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;

import java.beans.ConstructorProperties;
import java.util.List;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.util.Collections.emptyList;

@Data
public final class ConversationLookup {
	@NonNull
	private final List<String> conversationIds;

	@Builder
	@ConstructorProperties({"conversationIds"})
	private ConversationLookup(@Singular List<String> conversationIds) {
		this.conversationIds = firstNonNull(conversationIds, emptyList());
	}

	@JsonIgnore
	public boolean isEmpty() {
		return conversationIds.isEmpty();
	}
}

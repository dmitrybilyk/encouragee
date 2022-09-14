package com.encouragee.camel.clientSearch.conversation;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

@Data
public class ConversationList {

	@NotNull
	private final List<Conversation> conversations;

	public ConversationList() {
		this(new ArrayList<>());
	}

	@ConstructorProperties("conversations")
	public ConversationList(List<Conversation> conversations) {
		this.conversations = conversations != null ? conversations : new ArrayList<>();
	}

	public void add(Conversation conversation) {
		conversations.add(conversation);
	}
}

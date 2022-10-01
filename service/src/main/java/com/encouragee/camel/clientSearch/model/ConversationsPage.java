package com.encouragee.camel.clientSearch.model;

//import com.encouragee.camel.clientSearch.conversation.Conversation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zoomint.encourage.model.conversation.Conversation;
import lombok.*;

import java.beans.ConstructorProperties;
import java.util.List;

import static java.util.Collections.emptyList;

/**
 * Helper class to (un)marshal hal+json page data
 */
public class ConversationsPage extends HalPage {
	@JsonProperty("_embedded")
	private final Embedded embedded;
	@Getter
	private final List<Highlighting> highlighting;

	@ConstructorProperties({"_embedded", "page", "highlighting"})
	public ConversationsPage(Embedded embedded, PageMetadata page, List<Highlighting> highlighting) {
		super(page);
		this.embedded = embedded;
		this.highlighting = highlighting;
	}

	public ConversationsPage(List<Conversation> conversations) {
		this(new Embedded(conversations),
				new PageMetadata(conversations.size(), conversations.size(), 1, 0, null),
				null);
	}

	@JsonIgnore
	public List<Conversation> getConversations() {
		if (embedded == null || embedded.getConversations() == null) {
			return emptyList();
		}
		return embedded.getConversations();
	}

	public static class Embedded {
		private final List<Conversation> conversations;

		@ConstructorProperties("conversations")
		public Embedded(List<Conversation> conversations) {
			this.conversations = conversations;
		}

		public List<Conversation> getConversations() {
			return conversations;
		}
	}

	@Data
	@Builder
	@AllArgsConstructor
	public static class Highlighting {
		private final Conversation entity;
		@Singular
		private final List<Highlight> highlights;
	}

	@Data
	@Builder
	@AllArgsConstructor
	public static class Highlight {
		private final Field field;
		@Singular
		private final List<String> snipplets;
	}

	@Data
	@Builder
	@AllArgsConstructor
	public static class Field {
		private final String name;
	}
}

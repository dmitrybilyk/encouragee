package com.encouragee.camel.clientSearch.conversation.converter;

import com.encouragee.camel.clientSearch.model.ConversationEntities;
import com.encouragee.camel.clientSearch.model.ConversationSearchPermissions;
import com.encouragee.camel.clientSearch.model.ConversationUpdate;
import com.encouragee.camel.clientSearch.model.ConversationsPage;
import com.zoomint.encourage.common.model.search.SearchTemplateList;
import com.zoomint.encourage.model.conversation.Conversation;
import com.zoomint.encourage.model.conversation.ConversationEvent;
import com.zoomint.encourage.model.search.ClientConversationSearch;
import org.apache.camel.Converter;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

/**
 * Converts various entities/lists that reference other entities into a big list of all the referenced entities.
 * This is usually necessary to perform a bulk lookup of referenced entities.
 */
@Component
@Converter
@Profile("solr")
public class ConversationEntitiesConverter {

	@Converter
	@NotNull
	public ConversationEntities fromConversationsPage(@NotNull ConversationsPage page) {
		return new ConversationEntities().forEvents(
				page.getConversations().stream().flatMap(conv -> conv.getEvents().stream()));
	}

	@Converter
	@NotNull
	public ConversationEntities fromConversation(@NotNull Conversation conversation) {
		return new ConversationEntities().forEvents(conversation.getEvents().stream());
	}

	@Converter
	@NotNull
	public ConversationEntities fromEvent(@NotNull ConversationEvent event) {
		return new ConversationEntities().forEvents(Stream.of(event));
	}

	@Converter
	@NotNull
	public ConversationEntities fromSearch(@NotNull ClientConversationSearch search) {
		return new ConversationEntities().forSearch(search);
	}

	@Converter
	@NotNull
	public ConversationEntities fromSearchTemplates(@NotNull SearchTemplateList templates) {
		return new ConversationEntities().forSearchTemplates(templates);
	}

	@Converter
	@NotNull
	public ConversationEntities fromSearchPermissions(@NotNull ConversationSearchPermissions permissions) {
		return new ConversationEntities().forSearchPermissions(permissions);
	}

	@Converter
	@NotNull
	public ConversationEntities fromUpdate(@NotNull ConversationUpdate update) {
		return new ConversationEntities().forEvents(update.getConversations().stream().flatMap(conv -> conv.getEvents().stream()));
	}
}

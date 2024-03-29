package com.encouragee.camel.clientSearch.repository;

import com.encouragee.camel.clientSearch.model.ConversationSearchPermissions;
import com.encouragee.camel.clientSearch.model.EncourageSolrResultPage;
import com.encouragee.model.solr.ConversationDocument;
import com.zoomint.encourage.model.search.ClientConversationSearch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;


@Profile("solr")
public interface ConversationSearchRepositoryCustom {

	EncourageSolrResultPage<ConversationDocument> findConversations(
			@NotNull ClientConversationSearch search, @NotNull ConversationSearchPermissions filter, @NotNull ZonedDateTime clientTimestamp,
			@Nullable String cursorMark, @NotNull Pageable pageable, List<ClientConversationSearch> clientConversationSearches);

	EncourageSolrResultPage<ConversationDocument> findConversations(
			@NotNull ClientConversationSearch search, @NotNull ConversationSearchPermissions filter, @NotNull ZonedDateTime clientTimestamp,
			@Nullable String cursorMark, @NotNull Pageable pageable);

	EncourageSolrResultPage<ConversationDocument> findConversationsToReindexAfter(@Nullable String lastConversationId, @NotNull Pageable pageable);

	boolean checkConversationMatchesAnySearch(
			@NotNull String conversationId,
			@NotNull List<ClientConversationSearch> searches,
			@NotNull ZonedDateTime clientTimestamp);

	@NotNull
	EncourageSolrResultPage<ConversationDocument> findConversationsIncludeExcludeAfter(
			@NotNull List<ClientConversationSearch> includeSearches,
			@NotNull List<ClientConversationSearch> excludeSearches,
			@Nullable String lastConversationId,
			@NotNull ZonedDateTime clientTimestamp,
			@NotNull Pageable pageable);

	@NotNull
	List<ConversationDocument> findByConversationIdIn(@NotNull List<String> conversationIds);

}

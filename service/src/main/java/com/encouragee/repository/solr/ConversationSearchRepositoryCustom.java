//package com.encouragee.repository.solr;
//
//import com.encouragee.model.solr.ConversationDocument;
//import com.encouragee.model.solr.EncourageSolrResultPage;
//import org.springframework.data.domain.Pageable;
//
//import javax.annotation.Nullable;
//import javax.validation.constraints.NotNull;
//import java.time.ZonedDateTime;
//import java.util.List;
//
//
//public interface ConversationSearchRepositoryCustom {
//
//	EncourageSolrResultPage<ConversationDocument> findConversations(
//			@NotNull ClientConversationSearch search, @NotNull ConversationSearchPermissions filter, @NotNull ZonedDateTime clientTimestamp,
//			@Nullable String cursorMark, @NotNull Pageable pageable);
//
//	EncourageSolrResultPage<ConversationDocument> findConversationsToReindexAfter(@Nullable String lastConversationId, @NotNull Pageable pageable);
//
//	boolean checkConversationMatchesAnySearch(
//			@NotNull String conversationId,
//			@NotNull List<ClientConversationSearch> searches,
//			@NotNull ZonedDateTime clientTimestamp);
//
//	@NotNull
//	EncourageSolrResultPage<ConversationDocument> findConversationsIncludeExcludeAfter(
//			@NotNull List<ClientConversationSearch> includeSearches,
//			@NotNull List<ClientConversationSearch> excludeSearches,
//			@Nullable String lastConversationId,
//			@NotNull ZonedDateTime clientTimestamp,
//			@NotNull Pageable pageable);
//
//	@NotNull
//	List<ConversationDocument> findByConversationIdIn(@NotNull List<String> conversationIds);
//
//}

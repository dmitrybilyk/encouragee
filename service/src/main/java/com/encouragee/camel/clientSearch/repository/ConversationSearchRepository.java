package com.encouragee.camel.clientSearch.repository;

import com.encouragee.camel.clientSearch.model.ConversationDocument;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.Optional;

public interface ConversationSearchRepository
		extends SolrCrudRepository<ConversationDocument, String>, ConversationSearchRepositoryCustom {

	int PARAM_MAX_LENGTH = 1000;

	@Override
	Optional<ConversationDocument> findById(@NotNull String conversationId);

}

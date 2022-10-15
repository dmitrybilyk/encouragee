package com.encouragee.camel.clientSearch.repository;

import com.encouragee.model.solr.ConversationDocument;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Profile;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.Optional;

@Profile("solr")
public interface ConversationSearchRepository
		extends SolrCrudRepository<ConversationDocument, String>, ConversationSearchRepositoryCustom {

	int PARAM_MAX_LENGTH = 1000;

	@Override
	Optional<ConversationDocument> findById(@NotNull String conversationId);

}

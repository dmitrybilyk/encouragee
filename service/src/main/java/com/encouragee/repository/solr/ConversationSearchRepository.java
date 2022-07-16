//package com.encouragee.repository.solr;
//
//import com.encouragee.model.solr.ConversationDocument;
//import org.springframework.data.solr.repository.SolrCrudRepository;
//
//import javax.validation.constraints.NotNull;
//import java.util.Optional;
//
//public interface ConversationSearchRepository
//		extends SolrCrudRepository<ConversationDocument, String>, ConversationSearchRepositoryCustom {
//
//	int PARAM_MAX_LENGTH = 1000;
//
//	@Override
//	Optional<ConversationDocument> findById(@NotNull String conversationId);
//
//}

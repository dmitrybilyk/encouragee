package com.encouragee.camel.clientSearch.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.repository.query.SolrEntityInformation;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

/**
 * See
 * <a href="http://docs.spring.io/spring-data/commons/docs/current/reference/html/#repositories.custom-behaviour-for-all-repositories">
 * Spring Data Commons - Reference Documentation: Adding custom behavior to all repositories</a>
 */
@Profile("solr")
public class DefaultSolrRepository<T, ID extends Serializable> extends SimpleSolrRepository<T, ID> {

	private final String solrCollectionName;

	/**
	 * @param metadata must not be null
	 * @param solrOperations must not be null
	 */
	public DefaultSolrRepository(SolrOperations solrOperations, SolrEntityInformation<T, ?> metadata) {
		super(solrOperations, metadata);
		this.solrCollectionName = metadata.getCollectionName();
	}

	@Override
	public Optional<T> findById(ID id) {
		// use Realtime Get instead of query
		return getSolrOperations().getById(solrCollectionName, id, getEntityClass());
	}

	@Override
	public Iterable<T> findAllById(Iterable<ID> ids) {
		// use Realtime Get instead of query
		Collection<ID> idCollection = ids instanceof Collection ? (Collection<ID>) ids : newArrayList(ids.iterator());
		return getSolrOperations().getByIds(solrCollectionName, idCollection, getEntityClass());
	}
}

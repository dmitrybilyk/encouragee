package com.encouragee.camel.clientSearch.repository;

import com.encouragee.camel.clientSearch.model.EncourageSolrResultPage;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CursorMarkParams;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.util.Assert;

import static java.util.Collections.emptyList;
import static org.springframework.data.domain.Pageable.unpaged;

public class EncourageSolrTemplate extends SolrTemplate {

	public EncourageSolrTemplate(SolrClient solrClient) {
		super(solrClient);
	}


	public <T> EncourageSolrResultPage<T> queryForCursor(String collection, Query query, Class<T> clazz, String cursorMark) {
		Assert.notNull(query, "Query must not be 'null'");

		SolrQuery solrQuery = constructQuery(query, clazz);
		if (cursorMark != null) {
			solrQuery.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);
		}
		QueryResponse response = execute(solrServer -> solrServer.query(collection, solrQuery, SolrRequest.METHOD.POST));

		return createSolrResultPage(response, clazz);
	}

	private <T> EncourageSolrResultPage<T> createSolrResultPage(QueryResponse response, Class<T> clazz) {
		SolrDocumentList results = response.getResults();
		if (results == null) {
			return new EncourageSolrResultPage<>(emptyList(), unpaged(), 0, null, response.getNextCursorMark());
		}

		return new EncourageSolrResultPage<>(
				convertSolrDocumentListToBeans(results, clazz),
				unpaged(),
				results.getNumFound(),
				results.getMaxScore(),
				response.getNextCursorMark());
	}
}

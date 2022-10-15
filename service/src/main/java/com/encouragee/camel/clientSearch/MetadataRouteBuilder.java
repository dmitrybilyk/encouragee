package com.encouragee.camel.clientSearch;

import com.zoomint.encourage.model.EncourageConstants;
import com.zoomint.encourage.model.search.SearchableMetadata;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleTermsQuery;
import org.springframework.data.solr.core.query.TermsOptions.Sort;
import org.springframework.data.solr.core.query.result.CountEntry;
import org.springframework.data.solr.core.query.result.TermsFieldEntry;
import org.springframework.data.solr.core.query.result.TermsPage;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.encouragee.camel.clientSearch.conversation.converter.FieldNames.getMetadataFieldName;
import static com.encouragee.model.solr.ConversationDocument.SOLR_COLLECTION_NAME;
import static com.zoomint.encourage.model.search.SearchableMetadata.CompareOperator.EQUALS;
import static java.util.stream.Collectors.toList;

@Component
@Profile("solr")
public class MetadataRouteBuilder extends RouteBuilder {
	public static final String URI_METADATA_STARTS_WITH = "direct:metadataSearchStartsWith";

	private final SolrTemplate solrTemplate;

	public MetadataRouteBuilder(SolrTemplate solrTemplate) {
		this.solrTemplate = solrTemplate;
	}

	@Override
	public void configure() {
		errorHandler(noErrorHandler()); // propagate exceptions back to original route

		from(URI_METADATA_STARTS_WITH).routeId("metadataSearchStartsWith")
				.setBody().exchange(this::getValuesStartingWithForKey)
		;
	}

	private SearchableMetadata getValuesStartingWithForKey(Exchange exchange) {
		String mdKey = exchange.getProperty("key", String.class);
		String mdValuePrefix = exchange.getProperty("value", String.class);
		return SearchableMetadata.builder()
				.key(mdKey)
				.compareOperator(EQUALS)
				.values(getMetadataValuesStartingWith(mdKey, mdValuePrefix)
						// skip the anonymized value
						.filter(term -> !EncourageConstants.FIELD_ANONYMIZED_METADATA.equals(term))
						.collect(toList()))
				.build();
	}

	private Stream<String> getMetadataValuesStartingWith(String mdKey, String mdValuePrefix) {
		String fieldName = getMetadataFieldName(mdKey);
		SimpleTermsQuery query = SimpleTermsQuery.queryBuilder()
				.fields(fieldName)
				.sort(Sort.INDEX)
				.prefix(mdValuePrefix != null ? mdValuePrefix.toLowerCase(Locale.ROOT) : null)
				.build();

		TermsPage termsPage = solrTemplate.queryForTermsPage(SOLR_COLLECTION_NAME, query);
		Iterable<TermsFieldEntry> terms = termsPage.getTermsForField(fieldName);
		return StreamSupport.stream(terms.spliterator(), false)
			.map(CountEntry::getValue);
	}
}

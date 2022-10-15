package com.encouragee.camel.clientSearch;

import com.encouragee.camel.clientSearch.repository.ConversationSearchRepository;
import com.zoomint.encourage.common.exceptions.ProtectedConversationException;
import com.zoomint.encourage.common.model.search.SearchTemplate;
import com.zoomint.encourage.common.model.search.SearchTemplateList;
import com.zoomint.encourage.common.model.settings.EncourageSettings;
import com.zoomint.encourage.model.search.ClientConversationSearch;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.camel.util.toolbox.AggregationStrategies.flexible;

@Component
@Profile("solr")
public class SearchTemplateRouteBuilder extends RouteBuilder {

	public static final String URI_ENSURE_CONVERSATION_NOT_PROTECTED = "direct:ensureConversationNotProtected";

	private final ConversationSearchRepository repository;

	public SearchTemplateRouteBuilder(ConversationSearchRepository repository) {
		this.repository = repository;
	}

	@Override
	public void configure() {
		errorHandler(noErrorHandler()); // propagate exceptions back to original route

		onException(ProtectedConversationException.class)
				.logStackTrace(false)
				.retriesExhaustedLogLevel(LoggingLevel.DEBUG)
		;

		from(URI_ENSURE_CONVERSATION_NOT_PROTECTED).routeId("ensureConversationNotProtected")
				.enrich(DataAccessRouteBuilder.URI_GET_ADMIN_SETTINGS,
						flexible().storeInProperty("adminSettings"))
				.enrich(ConversationTaskRouteBuilder.URI_LOAD_PROTECTION_TEMPLATES,
						flexible().storeInProperty("protectionTemplates"))
				.process().exchange(this::performProtectionSearch)
		;
	}

	private void performProtectionSearch(Exchange exchange) {
		SearchTemplateList templateList = exchange.getProperty("protectionTemplates", SearchTemplateList.class);
		if (templateList.getSearchTemplates().isEmpty()) {
			return;
		}

		String conversationId = exchange.getProperty("conversationId", String.class);
		EncourageSettings settings = exchange.getProperty("adminSettings", EncourageSettings.class);

		List<ClientConversationSearch> protectionSearches = templateList.getSearchTemplates().stream()
				.map(SearchTemplate::getConversationSearch)
				.collect(toList());

		boolean isProtected = repository.checkConversationMatchesAnySearch(conversationId, protectionSearches, settings.getServerTimestamp());
		if (isProtected) {
			throw new ProtectedConversationException();
		}
	}

}

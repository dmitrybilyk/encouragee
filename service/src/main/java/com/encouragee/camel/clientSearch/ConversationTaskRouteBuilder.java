
package com.encouragee.camel.clientSearch;

import com.encouragee.camel.clientSearch.model.ConversationUpdate;
import com.encouragee.camel.clientSearch.repository.ConversationSearchRepository;
import com.encouragee.model.solr.ConversationDocument;
import com.zoomint.encourage.common.model.search.SearchTemplate;
import com.zoomint.encourage.common.model.search.SearchTemplateList;
import com.zoomint.encourage.common.model.settings.EncourageSettings;
import com.zoomint.encourage.model.conversation.Conversation;
import com.zoomint.encourage.model.conversation.ConversationEvent;
import com.zoomint.encourage.model.conversation.ConversationResource;
import com.zoomint.encourage.model.conversation.event.MediaEvent;
import com.zoomint.encourage.model.scheduler.ExternalRecord;
import com.zoomint.encourage.model.scheduler.ImportReport;
import com.zoomint.encourage.model.scheduler.ImportTask;
import com.zoomint.encourage.model.search.ClientConversationSearch;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static com.encouragee.ConversationProperties.SOURCE_ENCOURAGE;
import static com.encouragee.ConversationProperties.getSourceSystem;
import static com.encouragee.camel.clientSearch.TaskRouteBuilder.TASK;
import static com.encouragee.camel.clientSearch.TaskRouteBuilder.TASK_STATUS;
import static com.encouragee.model.solr.ConversationDocument.FIELD_CONVERSATION_ID;
import static com.encouragee.model.solr.ConversationDocument.FIELD_LATEST_SEGMENT_START_DATE_TIME;
import static com.google.common.base.Throwables.getRootCause;
import static com.google.common.collect.Iterables.getLast;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.camel.LoggingLevel.DEBUG;
import static org.apache.camel.util.toolbox.AggregationStrategies.flexible;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Component
@Profile("solr")
public class ConversationTaskRouteBuilder extends RouteBuilder {
	public static final String URI_START_DELETE = "seda:taskDeleteStart?waitForTaskToComplete=Never";
	public static final String URI_START_REINDEX = "seda:taskReindexStart?waitForTaskToComplete=Never";

	private static final String URI_LOAD_DELETION_TEMPLATES = "direct:taskLoadDeletionTemplates";
	public static final String URI_LOAD_PROTECTION_TEMPLATES = "direct:taskLoadProtectionTemplates";

	private final ConversationSearchRepository repository;

	public ConversationTaskRouteBuilder(ConversationSearchRepository repository) {

		this.repository = repository;
	}

	@Override
	public void configure() {
		errorHandler(noErrorHandler()); // propagate exceptions back to original route

		from(URI_START_REINDEX).routeId("taskReindexStart")
				.log("Starting reindex of conversation batch: ${body}")
				.to(TaskRouteBuilder.URI_INIT_TASK)

				.log(DEBUG, "Getting conversations to be reindexed")
				.setBody(this::findBatchToReindex)
				.enrichWith(ConversationUpdateRouteBuilder.URI_UPDATE_CONVERSATIONS, true).exchange(this::updateBatchStatus)
				.log("Finished reindex of conversation batch")
		;

		from(URI_START_DELETE).routeId("taskDeleteStart")
				.log("Starting deletion of conversations: ${body}")
				.to(TaskRouteBuilder.URI_INIT_TASK)

				.log(DEBUG, "Looking up deletion and protection saved searches")
				.enrich(DataAccessRouteBuilder.URI_GET_ADMIN_SETTINGS, flexible().storeInProperty("adminSettings"))
				.enrich(URI_LOAD_DELETION_TEMPLATES, flexible().storeInProperty("deletionTemplates"))
				.enrich(URI_LOAD_PROTECTION_TEMPLATES, flexible().storeInProperty("protectionTemplates"))

				.log(DEBUG, "Getting conversations to be deleted")
				.setBody(this::findBatchToDelete)
				.enrichWith(ConversationUpdateRouteBuilder.URI_UPDATE_CONVERSATIONS, true).exchange(this::updateBatchStatus)
				.log("Finished deleting conversations")
		;

		from(URI_LOAD_DELETION_TEMPLATES).routeId("taskLoadDeletionTemplates")
				.to(DataAccessRouteBuilder.URI_GET_DELETION_SEARCH_TEMPLATES)
				.to(EnrichRouteBuilder.URI_ENRICH_SEARCH_TEMPLATES);

		from(URI_LOAD_PROTECTION_TEMPLATES).routeId("taskLoadProtectionTemplates")
				.to(DataAccessRouteBuilder.URI_GET_PROTECTION_SEARCH_TEMPLATES)
				.to(EnrichRouteBuilder.URI_ENRICH_SEARCH_TEMPLATES);
	}

	private ConversationUpdate findBatchToReindex(Exchange exchange) {
		ImportTask task = exchange.getProperty(TASK, ImportTask.class);
		ImportReport status = exchange.getProperty(TASK_STATUS, ImportReport.class);
		status.setTimestamp(Instant.now());

		try {
			String lastConversationId = task.getFrom() != null ? task.getFrom().getExternalId() : null;
			PageRequest page = PageRequest.of(0, task.getSize(), DESC, FIELD_LATEST_SEGMENT_START_DATE_TIME);
			Page<ConversationDocument> documents = repository.findConversationsToReindexAfter(lastConversationId, page);

			addResultsToStatus(documents, status);

			return getConversationUpdate(documents);
		} catch (Exception exc) {
			status.setComplete(false);
			status.getErrors().add(getErrorMessage(exc));
			throw exc; // propagate to stop reindexing
		}
	}

	private ConversationUpdate findBatchToDelete(Exchange exchange) {
		ImportTask task = exchange.getProperty(TASK, ImportTask.class);
		ImportReport status = exchange.getProperty(TASK_STATUS, ImportReport.class);
		ZonedDateTime now = ZonedDateTime.now();
		status.setTimestamp(now.toInstant());

		try {
			SearchTemplateList deletionTemplates = exchange.getProperty("deletionTemplates", SearchTemplateList.class);
			if (deletionTemplates.getSearchTemplates().isEmpty()) {
				log.info("Deletion will not be performed - there are no deletion saved searches configured.");
				status.setComplete(true);
				return new ConversationUpdate(emptyList());
			}

			SearchTemplateList protectionTemplates = exchange.getProperty("protectionTemplates", SearchTemplateList.class);
			String lastConversationId = Optional.ofNullable(task.getFrom()).map(ExternalRecord::getExternalId).orElse(null);

			logDeletionSearch(deletionTemplates, protectionTemplates, lastConversationId);

			List<ClientConversationSearch> deletionSearches = getSearches(deletionTemplates);
			List<ClientConversationSearch> protectionSearches = getSearches(protectionTemplates);
			PageRequest page = PageRequest.of(0, task.getSize(), ASC, FIELD_CONVERSATION_ID);

			Page<ConversationDocument> documents = repository.findConversationsIncludeExcludeAfter(
					deletionSearches, protectionSearches, lastConversationId, now, page);

			addResultsToStatus(documents, status);

			ConversationUpdate update = getConversationUpdate(documents);
			update.setModifications(conversation -> deleteMediaEvents(conversation, update));
			return update;
		} catch (Exception exc) {
			status.setComplete(false);
			status.getErrors().add(getErrorMessage(exc));
			throw exc; // propagate to stop reindexing
		}
	}

	private void logDeletionSearch(SearchTemplateList deletionTemplates, SearchTemplateList protectionTemplates, String lastConversationId) {
		if (log.isInfoEnabled()) {
			log.info("Deletion will be performed from {} based on {} deletion searches and {} protection searches (see debug for details)",
					lastConversationId != null ? lastConversationId : "the start",
					deletionTemplates.getSearchTemplates().size(),
					protectionTemplates.getSearchTemplates().size());
		}

		if (log.isDebugEnabled()) {
			deletionTemplates.getSearchTemplates().forEach(template ->
					log.debug("Deletion search \"{}\": {}", template.getName(), template.getConversationSearch()));
			protectionTemplates.getSearchTemplates().forEach(template ->
					log.debug("Protection search \"{}\": {}", template.getName(), template.getConversationSearch()));
		}
	}

	private void deleteMediaEvents(Conversation conversation, ConversationUpdate update) {
		conversation.getEvents().removeIf(event -> {
			if (event instanceof MediaEvent && shouldDeleteImmediately(event)) {
				update.getDeletedEvents().add(event);
				return true;
			} else if (event instanceof MediaEvent) {
				markDeleteRequested(event);
				update.getChangedEvents().add(event);
			}
			return false;
		});
	}

	private void markDeleteRequested(ConversationEvent event) {
		ConversationResource resourceToFlag = event.getResource();
		while (resourceToFlag != null) {
			resourceToFlag.getFlags().add(ConversationResource.FLAG_DELETE_REQUESTED);
			resourceToFlag = resourceToFlag.getParent();
		}
	}

	private boolean shouldDeleteImmediately(ConversationEvent event) {
		// no source system => local => delete immediately
		return SOURCE_ENCOURAGE.equals(getSourceSystem(event.getEventId()));
	}

	private void addResultsToStatus(Page<ConversationDocument> documents, ImportReport status) {
		status.setComplete(documents.getTotalPages() <= 1);
		if (!documents.isEmpty()) {
			status.setFrom(ExternalRecord.builder()
					.externalId(documents.getContent().get(0).getConversationId())
					.build());
			status.setTo(ExternalRecord.builder()
					.externalId(getLast(documents.getContent()).getConversationId())
					.build());
		}
	}

	private ConversationUpdate getConversationUpdate(Page<ConversationDocument> documents) {
		ConversationUpdate update = new ConversationUpdate(documents.getContent().stream()
				.map(doc -> Conversation.builder()
						.conversationId(doc.getConversationId())
						// and no new events or previousIds - everything is from the documents
						.build())
				.collect(toList()));

		// also skip loading these documents since they are already loaded!
		documents.getContent().stream()
				.peek(doc -> log.trace("Loaded document: {} (version {})", doc.getConversationId(), doc.getVersion()))
				.forEach(doc -> update.getDocumentById().put(doc.getConversationId(), doc));

		return update;
	}

	private List<ClientConversationSearch> getSearches(SearchTemplateList templateList) {
		return templateList.getSearchTemplates().stream()
				.map(SearchTemplate::getConversationSearch)
				.collect(toList());
	}

	private Exchange updateBatchStatus(Exchange previous, Exchange saveResult) {
		ConversationUpdate saved = previous.getIn().getBody(ConversationUpdate.class);
		ImportReport status = previous.getProperty(TASK_STATUS, ImportReport.class);
		if (!saveResult.isFailed()) {
			status.setSize(saved.getConversations().size());
		} else {
			status.getErrors().add(getErrorMessage(saveResult.getException()));
		}
		return previous;
	}

	private String getErrorMessage(Exception exception) {
		if (exception == null) {
			return "Unknown";
		}
		Throwable rootCause = getRootCause(exception);
		return rootCause.getClass().getSimpleName()
				+ (rootCause.getMessage() != null ? ": " + rootCause.getMessage() : "");
	}
}

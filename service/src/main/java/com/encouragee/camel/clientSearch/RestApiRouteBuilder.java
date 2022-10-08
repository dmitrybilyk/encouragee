package com.encouragee.camel.clientSearch;

import com.encouragee.camel.clientSearch.model.ConversationsPage;
import com.zoomint.encourage.common.camel.restlet.RestletUtil;
import com.zoomint.encourage.model.conversation.Conversation;
import com.zoomint.encourage.model.conversation.ConversationEvent;
import com.zoomint.encourage.model.scheduler.ImportTask;
import com.zoomint.encourage.model.scheduler.validation.ImportTaskCheck;
import com.zoomint.encourage.model.search.ClientConversationSearch;
import com.zoomint.encourage.model.search.SearchableMetadata;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static java.net.HttpURLConnection.*;
import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;
import static org.apache.camel.LoggingLevel.DEBUG;
import static org.apache.camel.LoggingLevel.ERROR;
import static org.apache.camel.model.rest.RestParamType.*;

@Component
public class RestApiRouteBuilder extends RouteBuilder {
	private static final String APPLICATION_JSON_UTF_8 = "application/json;charset=UTF-8";

	@Override
	public void configure() {
		RestletUtil.outgoingRestCallsExceptionHandling(this);

		rest("/api/v3/conversations")

				.post("/client-search")
					.description("Searches conversations based on relative time inputs, e.g.,"
							+ " YESTERDAY or PREVIOUS_MONTH will be recalculated at the moment of search."
							+ " Also converts date time values based on user timezone.")

					.param().type(query).name("page").required(false).dataType("integer")
						.description("The page number to access. Default is 0").endParam()
					.param().type(query).name("size").required(false).dataType("integer")
						.description("The page size requested. Default is 20. The maximum is 1000").endParam()
					.param().type(query).name("cursorMark").required(false).dataType("string")
						.description("CursorMark is used when fetching a large number of sorted results. See Solr documentation for cursors." +
								"First search should have cursorMark value *, cursorMark for next data is part of page response." +
								"If cursorMark is used, then page number must be 0 and one of sort parameters must be unique (conversationID)." +
								"Highlighting is not supported when cursor is used.").endParam()
					.param().type(body).name("body").description("The search object").endParam()
					.consumes(APPLICATION_JSON_UTF_8).type(ClientConversationSearch.class)

					.responseMessage().code(HTTP_OK).message("A page of conversations that match the specified criteria").endResponseMessage()
					.produces(APPLICATION_JSON_UTF_8).outType(ConversationsPage.class)

				.route().routeId("POST@v3/conversations/client-search")
					.removeHeaders("*")
					.to(ConversationSearchRouteBuilder.URI_SEARCH_CONVERSATIONS)

				.endRest()

				.get("/{conversationId}")
					.description("Return conversation with the specified ID")

					.param().type(path).name("conversationId").dataType("string").description("ID of the conversation").endParam()

					.responseMessage().code(HTTP_OK).message("The conversation").endResponseMessage()
					.produces(APPLICATION_JSON_UTF_8).outType(Conversation.class)

				.route().routeId("GET@v3/conversations/{id}")
					.validate(header("conversationId").isNotNull())
					.setProperty("conversationId").header("conversationId")
					.removeHeaders("*")
					.log(DEBUG, "Conversation requested: ${exchangeProperty.conversationId}")
					.to(ConversationSearchRouteBuilder.URI_GET_CONVERSATION)

				.endRest()

				.post("/{conversationId}/events")
					.description("Allows to save single event to given conversation.")
					.param().type(path).name("conversationId").dataType("string")
						.description("The conversation ID of conversation where event will be added.").endParam()
					.param().type(body).name("body").dataType("string")
						.description("Events to be inserted. All events must include event id. And new events must not include created date")
						.endParam()
					.consumes(APPLICATION_JSON_UTF_8).type(ConversationEvent.class)

					.responseMessage().code(HTTP_OK).message("Success of event save.").responseModel(Conversation.class).endResponseMessage()
					.produces(APPLICATION_JSON_UTF_8).outType(Conversation.class)

				.route().routeId("POST@v3/conversations/{id}/events")
					.validate(header("conversationId").isNotNull())
					.setProperty("conversationId").header("conversationId")
					.removeHeaders("*")
					.to("bean-validator:get-your-ass-to-mars")
					.validate().body(ConversationEvent.class, event -> event.getCreated() != null && event.getCreatedBy() != null)
					.log(DEBUG, "Conversation ${exchangeProperty.conversationId} will get a new event: ${body}")
					.to(ConversationUpdateRouteBuilder.URI_ADD_EVENT_TO_CONVERSATION)
					.setHeader(HTTP_RESPONSE_CODE, constant(HTTP_CREATED))

				.endRest()

				.delete("/")
				.route().routeId("DELETE@v3/conversations")
					.removeHeaders("*")
					.log(ERROR, "Deleting ALL conversations - test purposes only!")
					.to(RabbitMQRouteBuilder.URI_PURGE_QUEUES)
					.to(ConversationIndexingRouteBuilder.URI_DELETE_ALL)
				.endRest()
		;

		rest("/api/v2/metadata")
				.get("/search/starts-with")
					.description("Finds metadata values for one key that start with the specified string.")
					.param().type(query).name("key").required(true).dataType("string")
						.description("The key for which to find metadata values").endParam()
					.param().type(query).name("value").required(false).dataType("string")
						.description("The beginning of the value to search for").endParam()
					.produces(APPLICATION_JSON_UTF_8).outType(SearchableMetadata.class)
				.route().routeId("GET@v2/metadata/search/starts-with")
					.validate(header("key").isNotNull())
					.setProperty("key", header("key"))
					.setProperty("value", header("value"))
					.removeHeaders("*")
					.to(MetadataRouteBuilder.URI_METADATA_STARTS_WITH)
		;

		rest("/api/v2/delete")
				.post()
					.description("Deletes conversations based on deletion and protection saved searches.")
					.consumes(APPLICATION_JSON_UTF_8).type(ImportTask.class)
				.route().routeId("POST@v2/delete")
					.removeHeaders("*")
					.to("bean-validator:get-to-the-choppa?group=" + ImportTaskCheck.class.getName())
					.to(ConversationTaskRouteBuilder.URI_START_DELETE)
					.setHeader(HTTP_RESPONSE_CODE, constant(HTTP_ACCEPTED))
		;

		rest("/api/v2/reindex")
				.post()
					.description("Re-indexes conversations that are not indexed based on current schema version.")
					.consumes(APPLICATION_JSON_UTF_8).type(ImportTask.class)
				.route().routeId("POST@v2/reindex")
					.removeHeaders("*")
					.to("bean-validator:get-to-the-choppa?group=" + ImportTaskCheck.class.getName())
					.to(ConversationTaskRouteBuilder.URI_START_REINDEX)
					.setHeader(HTTP_RESPONSE_CODE, constant(HTTP_ACCEPTED))
		;
	}
}

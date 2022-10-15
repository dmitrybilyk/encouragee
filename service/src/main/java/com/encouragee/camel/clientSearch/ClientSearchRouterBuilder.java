package com.encouragee.camel.clientSearch;

import com.encouragee.camel.clientSearch.model.ConversationsPage;
import com.zoomint.encourage.model.search.ClientConversationSearch;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.apache.camel.model.rest.RestParamType.body;
import static org.apache.camel.model.rest.RestParamType.query;

@Component
@Profile("solr")
public class ClientSearchRouterBuilder extends RouteBuilder {
    private static final String APPLICATION_JSON_UTF_8 = "application/json;charset=UTF-8";

    @Override
    public void configure() throws Exception {
        rest("/v3/conversations")

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

                .endRest();
    }
}

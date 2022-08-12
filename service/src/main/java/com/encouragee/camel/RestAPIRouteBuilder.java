package com.encouragee.camel;

import com.encouragee.camel.model.Chat;
import com.encouragee.camel.model.Email;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zoomint.encourage.common.exceptions.security.AuthorizationException;
//import com.zoomint.encourage.integrations.api.model.Chat;
//import com.zoomint.encourage.integrations.api.model.Email;
import com.zoomint.encourage.model.conversation.Conversation;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.ListJacksonDataFormat;
import org.apache.camel.model.rest.RestParamType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static java.net.HttpURLConnection.*;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.apache.camel.LoggingLevel.INFO;
import static org.apache.camel.LoggingLevel.TRACE;
import static org.apache.camel.model.rest.RestParamType.body;
import static org.restlet.engine.header.HeaderConstants.ATTRIBUTE_HEADERS;
import static org.restlet.engine.header.HeaderConstants.HEADER_AUTHORIZATION;

/**
 * Routes for exposing mapper functionality via REST API.
 */
@Component
public class RestAPIRouteBuilder extends RouteBuilder {
	private static final Logger LOG = LoggerFactory.getLogger(RestAPIRouteBuilder.class);

	private final ObjectMapper objectMapper;

	public RestAPIRouteBuilder(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void configure() {
		ListJacksonDataFormat jsonEmailList = new ListJacksonDataFormat(objectMapper, Email.class);
		ListJacksonDataFormat jsonChatList = new ListJacksonDataFormat(objectMapper, Chat.class);

		rest("/api/status")
				.get()
					.description("Gets the status of the service")
					.responseMessage().code(HTTP_NO_CONTENT).message("If the service is working propely").endResponseMessage()
				.route().routeId("GET@/status")
					.log(INFO, "Status called")
				.endRest();

		rest("/api/email")
				.post()
					.description("Inserts an email in the data storage.").type(Email[].class)
					.param().name("body").type(body).description("The emails to insert. Must be enclosed in [] as it is a list.").endParam()
					.param().name("Authorization").type(RestParamType.header).description("Authorization header. Type Bearer").endParam()
					.responseMessage().code(HTTP_OK).message("Email inserted successfully").endResponseMessage()
					.responseMessage()
						.code(HTTP_UNAUTHORIZED).message("If the authorization token is not valid, or the authorization header is missing")
					.endResponseMessage()
					.responseMessage()
						.code(HTTP_BAD_REQUEST).message("If the request body is empty, or " +
							"if authorization header is not of the valid format 'Bearer <token>'")
					.endResponseMessage()
					.produces(APPLICATION_JSON)
				.route().routeId("POST@/emails")
					.process(this::verifyAuthorizationToken)
					.removeHeaders(ATTRIBUTE_HEADERS) // remove HTTP headers from incoming request
					.validate(body().isNotNull())
					.unmarshal(jsonEmailList)
					.to(ImportConversationsRouteBuilder.URI_SAVE_EMAILS)
					.setBody(constant(null))
				.endRest();

		rest("/api/chat")
				.post()
					.description("Inserts a chat in the data storage.").type(Chat[].class)
					.param().name("body").type(body).description("The chats to insert. Must be enclosed in [] as it is a list.").endParam()
					.param().name("Authorization").type(RestParamType.header).description("Authorization header. Type Bearer").endParam()
					.responseMessage().code(HTTP_OK).message("Chat inserted successfully").endResponseMessage()
					.responseMessage()
						.code(HTTP_UNAUTHORIZED).message("If the authorization token is not valid, or the authorization header is missing")
					.endResponseMessage()
					.responseMessage()
						.code(HTTP_BAD_REQUEST).message("If the request body is empty, or " +
							"if authorization header is not of the valid format 'Bearer <token>'")
					.endResponseMessage()
					.produces(APPLICATION_JSON)
				.route().routeId("POST@/chats")
					.process(this::verifyAuthorizationToken)
					.removeHeaders(ATTRIBUTE_HEADERS) // remove HTTP headers from incoming request
					.validate(body().isNotNull())
					.unmarshal(jsonChatList)
					.to(ImportConversationsRouteBuilder.URI_SAVE_CHATS)
					.setBody(constant(null))
				.endRest();

		rest("/api/conversations")
				.post()
					.description("Sends back the same posted conversation. " +
							"Intended for mappers that don't support post conversation.").type(Conversation.class)
					.consumes(APPLICATION_JSON)
					.responseMessage().code(HTTP_OK).message("Conversation inserted successfully").endResponseMessage()
					.responseMessage()
						.code(HTTP_BAD_REQUEST).message("If the request body is empty, or " +
							"it has an invalid format")
					.endResponseMessage()
				.route().routeId("POST@/conversations")
					.removeHeaders(ATTRIBUTE_HEADERS) // remove HTTP headers from incoming request
					.validate(body().isNotNull())
					.log(TRACE, "Received conversations: ${body}")
				.endRest();
	}

	private void verifyAuthorizationToken(Exchange exchange) {
		if (exchange.getIn().getHeader(HEADER_AUTHORIZATION, String.class) == null) {
			LOG.error("No authorization header found.");
			throw new AuthorizationException();
		}
	}
}

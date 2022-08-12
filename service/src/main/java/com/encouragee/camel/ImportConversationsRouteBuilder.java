package com.encouragee.camel;

import com.encouragee.ApiProperties;
import com.encouragee.camel.converter.ConversationEntities;
import com.encouragee.camel.model.Chat;
import com.encouragee.camel.model.Email;
import com.zoomint.encourage.common.exceptions.security.AuthorizationException;
//import com.zoomint.encourage.integrations.api.ApiProperties;
//import com.zoomint.encourage.integrations.api.converter.ConversationEntities;
//import com.zoomint.encourage.integrations.api.model.Chat;
//import com.zoomint.encourage.integrations.api.model.Email;
import com.zoomint.encourage.model.conversation.Conversation;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.encouragee.camel.RabbitMQRouteBuilder.URI_CORRELATE_CONVERSATIONS;
import static com.encouragee.camel.converter.ConversationEntities.ENTITIES_PROPERTY;
//import static com.zoomint.encourage.common.camel.restlet.EncourageHeaderConstants.BEARER_SCHEME;
//import static com.zoomint.encourage.integrations.api.converter.ConversationEntities.ENTITIES_PROPERTY;
//import static com.zoomint.encourage.integrations.api.route.RabbitMQRouteBuilder.URI_CORRELATE_CONVERSATIONS;
import static org.apache.camel.LoggingLevel.DEBUG;
import static org.apache.camel.util.toolbox.AggregationStrategies.flexible;
import static org.restlet.engine.header.HeaderConstants.HEADER_AUTHORIZATION;

/**
 * Routes for processing an import task for conversations.
 */
@Component
public class ImportConversationsRouteBuilder extends RouteBuilder {

	public static final String BEARER_SCHEME = "Bearer ";

	public static final String URI_SAVE_EMAILS = "direct:importConvSaveEmails";
	public static final String URI_SAVE_CHATS = "direct:importConvSaveChats";
	private static final String URI_CONVERT_TO_CONVERSATIONS = "direct:importConvert";

	private final ApiProperties properties;

	public ImportConversationsRouteBuilder(ApiProperties properties) {
		this.properties = properties;
	}

	@Override
	public void configure() {
		from(URI_SAVE_EMAILS).routeId("importConvSaveEmails")
				.process(this::verifyAuthToken)
				.process(this::initEmails)
				.to(URI_CONVERT_TO_CONVERSATIONS)
				.log(DEBUG, "Saving emails converted to conversations")
				.to(EventsRouteBuilder.URI_SAVE_EVENTS)
				.to(URI_CORRELATE_CONVERSATIONS)
		;

		from(URI_SAVE_CHATS).routeId("importConvSaveChats")
				.process(this::verifyAuthToken)
				.process(this::initChats)
				.to(URI_CONVERT_TO_CONVERSATIONS)
				.log(DEBUG, "Saving chats converted to conversations")
				.to(EventsRouteBuilder.URI_SAVE_EVENTS)
				.to(URI_CORRELATE_CONVERSATIONS)
		;

		from(URI_CONVERT_TO_CONVERSATIONS).routeId("importConvert")
				// lookup entities necessary for conversion
				.to(EntitiesLookupRouteBuilder.URI_LOOKUP_ENTITIES)
				// use Camel converters to convert each element to Conversation
				.log(DEBUG, "Converting emails/chats to conversations")

				.split(body()).stopOnException()
				.aggregationStrategy(flexible().accumulateInCollection(ArrayList.class).storeInBody())
					.convertBodyTo(Conversation.class)
		;
	}

	@SuppressWarnings("unchecked")
	private void initEmails(Exchange exchange) {
		exchange.setProperty(ENTITIES_PROPERTY, new ConversationEntities()
				.forEmails((List<Email>) exchange.getIn().getBody(List.class)));
	}

	@SuppressWarnings("unchecked")
	private void initChats(Exchange exchange) {
		exchange.setProperty(ENTITIES_PROPERTY, new ConversationEntities()
				.forChats((List<Chat>) exchange.getIn().getBody(List.class)));
	}

	private void verifyAuthToken(Exchange exchange) {
		String tokenHeader = exchange.getIn().getHeader(HEADER_AUTHORIZATION, "", String.class);
		if (!tokenHeader.startsWith(BEARER_SCHEME)) {
			log.error("Only Bearer authorization scheme is supported.");
			throw new AuthorizationException();
		}
		if (!properties.getAuthorizationToken().equals(tokenHeader.substring(BEARER_SCHEME.length()).trim())) {
			log.error("Authorization token mismatch.");
			throw new AuthorizationException();
		}
	}
}

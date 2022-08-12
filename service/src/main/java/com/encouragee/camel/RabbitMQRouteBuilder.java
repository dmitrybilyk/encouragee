package com.encouragee.camel;

import com.encouragee.ApiProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.zoomint.encourage.integrations.api.ApiProperties;
import com.zoomint.encourage.model.conversation.ConversationList;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;

import static org.apache.camel.LoggingLevel.DEBUG;
import static org.apache.camel.component.rabbitmq.RabbitMQConstants.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * Routes for RabbitMQ messages messages.
 */
@Component
public class RabbitMQRouteBuilder extends RouteBuilder {

	public static final String URI_CORRELATE_CONVERSATIONS = "direct:rmqSaveConversations";

	private final ApiProperties properties;
	private final ObjectMapper objectMapper;

	public RabbitMQRouteBuilder(ApiProperties properties, ObjectMapper objectMapper) {
		this.properties = properties;
		this.objectMapper = objectMapper;
	}

	@Override
	public void configure() {
		from(URI_CORRELATE_CONVERSATIONS).routeId("rmqSaveConversations")
				.convertBodyTo(ConversationList.class)
				.log(DEBUG, "Sending ${body.conversations.size} conversations to be saved")
				.marshal(new JacksonDataFormat(objectMapper, Object.class))
				.setHeader(EXCHANGE_NAME, constant(properties.getCorrelationQueue().getExchange()))
				.setHeader(ROUTING_KEY, constant(properties.getCorrelationQueue().getName()))
				.setHeader(CONTENT_TYPE, constant(APPLICATION_JSON_UTF8_VALUE))
				.inOnly(rabbitUri(properties.getCorrelationQueue())
						+ "&declare=false"
						+ "&publisherAcknowledgements=true"
						+ "&publisherAcknowledgementsTimeout=" + properties.getPublishTimeout().toMillis())
				.removeHeaders("*") // remove headers from previous request
				.log(DEBUG, "Sent conversations to " + properties.getCorrelationQueue().getName())
		;
	}

	private String rabbitUri(ApiProperties.QueueProperties queue) {
		return "rabbitmq:" + queue.getExchange()
				+ "?queue=" + queue.getName()
				+ "&autoDelete=false"
				+ "&autoAck=false"
				+ "&prefetchEnabled=true"
				+ "&prefetchCount=100";
	}
}

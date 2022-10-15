package com.encouragee.camel.clientSearch;

import com.encouragee.ConversationProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zoomint.encourage.model.conversation.ConversationList;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static org.apache.camel.LoggingLevel.DEBUG;
import static org.apache.camel.LoggingLevel.TRACE;
import static org.apache.camel.component.rabbitmq.RabbitMQConstants.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * Routes for RabbitMQ messages messages.
 */
@Component
@Profile("solr")
public class RabbitMQRouteBuilder extends RouteBuilder {

	public static final String URI_PURGE_QUEUES = "direct:rmqClearQueues";
	public static final String URI_CONVERSATIONS_DELETED = "direct:rmqConversationsDeleted";

	private static final String URI_DEAD_LETTER = "direct:rmqDeadLetter";

	private static final String ROUTE_INDEX_CONVERSATIONS = "rmqIndexConversations";
	private static final String ROUTE_INDEX_CONVERSATIONS_ERROR = "rmqIndexConversationsError";

	private static final String METRICS_SUBSYSTEM = "conversation_indexer";

	private final ConversationProperties properties;
	private final AmqpAdmin rabbitAdmin;
	private final ObjectMapper objectMapper;
	private final Counter batchesTotal;

	public RabbitMQRouteBuilder(
			ConversationProperties properties,
			AmqpAdmin rabbitAdmin,
			ObjectMapper objectMapper,
			CollectorRegistry collectorRegistry) {

		this.properties = properties;
		this.rabbitAdmin = rabbitAdmin;
		this.objectMapper = objectMapper;

		batchesTotal = new Counter.Builder()
				.subsystem(METRICS_SUBSYSTEM)
				.name("batches_total")
				.help("Conversation Indexer - total number of batches received and processed via RabbitMQ API")
				.register(collectorRegistry);
	}

	@Override
	public void configure() {
		errorHandler(noErrorHandler()); // propagate exceptions back to original route

		from(URI_PURGE_QUEUES).routeId("rmqClearQueues")
				.onCompletion().process(this::restart).end()
				.process(this::stopAndClear)
		;

		from(URI_CONVERSATIONS_DELETED).routeId("rmqConversationsDeleted")
				.convertBodyTo(ConversationList.class)
				.filter().body(ConversationList.class, deleted -> !deleted.getConversations().isEmpty())

				.marshal(new JacksonDataFormat(objectMapper, ConversationList.class))
				.log(TRACE, "Sending deleted conversations to " + properties.getConversationDeletedQueue() + ": ${body}")

				.setHeader(EXCHANGE_NAME, constant("amq.direct"))
				.setHeader(ROUTING_KEY, constant(properties.getConversationDeletedQueue()))
				.setHeader(CONTENT_TYPE, constant(APPLICATION_JSON_UTF8_VALUE))
				.inOnly(rabbitUri(properties.getConversationDeletedQueue()) + "&declare=false")
				.removeHeaders("*") // remove headers from previous request
				.log(DEBUG, "Sent deleted conversations to " + properties.getConversationDeletedQueue())
		;

		from(rabbitUri(properties.getIndexTaskQueue())).routeId(ROUTE_INDEX_CONVERSATIONS)
				.errorHandler(deadLetterChannel(URI_DEAD_LETTER)
						.disableRedelivery() // all retries are via error queue
						.useOriginalMessage() // send the original message to error queue
						.logHandled(true) // errors should be logged
						.deadLetterHandleNewException(false)) // nack messages that cannot be sent to error queue
				.onCompletion().process(exchange -> batchesTotal.inc()).end()

				.removeHeaders("*")
				.log(TRACE, "Received message content: ${body}")
				.unmarshal(new JacksonDataFormat(objectMapper, ConversationList.class))

				.log(DEBUG, "Received ${body.conversations.size} conversations for indexing")
				.to(ConversationUpdateRouteBuilder.URI_UPDATE_CONVERSATIONS)

				.log(DEBUG, "Successfully finished processing message from default queue")
		;

		from(rabbitUri(properties.getIndexErrorQueue())).routeId(ROUTE_INDEX_CONVERSATIONS_ERROR)
				.log(DEBUG, "Re-queueing failed message back to default queue after a delay of " + properties.getDelayOnError())
				.log(TRACE, "Failed message body: ${body}")
				.delay(properties.getDelayOnError().toMillis())

				.log(DEBUG, "Re-queueing failed message back to default queue")
				.inOnly(rabbitUri(properties.getIndexTaskQueue()))

				.log(DEBUG, "Re-queued failed message back to default queue")
		;

		from(URI_DEAD_LETTER).routeId("rmqDeadLetter")
				.setHeader(RabbitMQConstants.REQUEUE, constant(true)) // requeue on nack

				.log(DEBUG, "Sending message to error queue")
				.inOnly(rabbitUri(properties.getIndexErrorQueue()))

				.log(DEBUG, "Sent message to error queue")
		;
	}

	private void restart(Exchange exchange) throws Exception {
		CamelContext context = exchange.getContext();
		log.debug("Restarting RabbitMQ indexing routes");
		context.startRoute(ROUTE_INDEX_CONVERSATIONS_ERROR);
		context.startRoute(ROUTE_INDEX_CONVERSATIONS);
	}

	private void stopAndClear(Exchange exchange) throws Exception {
		CamelContext context = exchange.getContext();
		log.debug("Stopping RabbitMQ indexing routes");
		context.stopRoute(ROUTE_INDEX_CONVERSATIONS, 100, TimeUnit.MILLISECONDS);
		context.stopRoute(ROUTE_INDEX_CONVERSATIONS_ERROR, 100, TimeUnit.MILLISECONDS);
		log.debug("Purging RabbitMQ indexing queues");
		rabbitAdmin.purgeQueue(properties.getIndexTaskQueue(), false);
		rabbitAdmin.purgeQueue(properties.getIndexErrorQueue(), false);
	}

	private String rabbitUri(String queue) {
		return "rabbitmq:amq.direct"
				+ "?queue=" + queue
				+ "&autoDelete=false"
				+ "&autoAck=false"
				+ "&publisherAcknowledgements=true"
				+ "&publisherAcknowledgementsTimeout=" + properties.getPublishTimeout().toMillis()
				+ "&prefetchEnabled=true"
				+ "&prefetchCount=2"
				+ "&concurrentConsumers=" + properties.getConsumerCount();
	}
}

package com.encouragee.rabbit.configuration;

import com.encouragee.messaging.Receiver;
import com.encouragee.rabbit.SimpleMessage;
import com.encouragee.rabbit.model.Conversation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.springframework.amqp.core.BindingBuilder.bind;

@Configuration
@ComponentScan(basePackages = {"com.encouragee.controller", "com.encouragee.messaging", "com.encouragee.camel",
        "com.encouragee.rabbit.controller"})
public class RabbitMQConfiguration {
    public static final String queueNameForDirectExchange = "queue.for.direct.exchange";
    public static final String topicExchangeName = "topic-exchange";
    public static final String COM_ENCOURAGEE_MESSAGING_QUEUE_TOPIC_1 = "com.encouragee.messaging.queueTopic1";
    public static final String COM_ENCOURAGEE_MESSAGING_QUEUE_TOPIC_2 = "com.encouragee.messaging.queueTopic2";
    public static final String COM_ENCOURAGEE_MESSAGING_QUEUE_TOPIC_3 = "com.encouragee.messaging.queueTopic3";

    @Bean
    Queue queue() {
        return new Queue(queueNameForDirectExchange, false);
    }

    @RabbitListener(queues = { queueNameForDirectExchange })
    public void receiveMessageFromDirect(String message) {
        System.out.println("Received direct message (" + queueNameForDirectExchange + ") message: " + message);
    }

//    Topic Exchange -----------------------------------------------------------------

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Queue queueTopic1() {
        return new Queue(COM_ENCOURAGEE_MESSAGING_QUEUE_TOPIC_1, false);
    }

    @Bean
    Queue queueTopic2() {
        return new Queue(COM_ENCOURAGEE_MESSAGING_QUEUE_TOPIC_2, false);
    }

    @Bean
    Queue queueTopic3() {
        return new Queue(COM_ENCOURAGEE_MESSAGING_QUEUE_TOPIC_3, false);
    }

    @Bean
    Binding binding1(Queue queueTopic1, TopicExchange exchange){
        return bind(queueTopic1).to(exchange).with("com.encouragee.messaging.first.#");
    }

    @Bean
    Binding binding2(Queue queueTopic2, TopicExchange exchange){
        return bind(queueTopic2).to(exchange).with("com.encouragee.messaging.multi.#");
    }

    @Bean
    Binding binding3(Queue queueTopic3, TopicExchange exchange){
        return bind(queueTopic3).to(exchange).with("com.encouragee.messaging.multi.#");
    }

    @RabbitListener(queues = { COM_ENCOURAGEE_MESSAGING_QUEUE_TOPIC_1 })
    public void receiveMessageFromTopicQueue1(Conversation message) {
        System.out.println("Received direct message (" + COM_ENCOURAGEE_MESSAGING_QUEUE_TOPIC_1 + ") message: " + message);
    }

    @RabbitListener(queues = { COM_ENCOURAGEE_MESSAGING_QUEUE_TOPIC_2 })
    public void receiveMessageFromTopicQueue2(Conversation message) {
        System.out.println("Received direct message (" + COM_ENCOURAGEE_MESSAGING_QUEUE_TOPIC_2 + ") message: " + message);
    }

    @RabbitListener(queues = { COM_ENCOURAGEE_MESSAGING_QUEUE_TOPIC_3 })
    public void receiveMessageFromTopicQueue3(Conversation message) {
        System.out.println("Received direct message (" + COM_ENCOURAGEE_MESSAGING_QUEUE_TOPIC_3 + ") message: " + message);
    }

//    @Bean
//    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter){
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(queueNameForDirectExchange);
//        container.setMessageListener(listenerAdapter);
//        return container;
//    }
//
//    @Bean
//    MessageListenerAdapter listenerAdapter(Receiver receiver) {
//        return new MessageListenerAdapter(receiver,"receiveMessage");
//    }
//
//    public void listen(String in) {
//        System.out.println("Message read from " + queueNameForDirectExchange + " : " + in);
//    }
}
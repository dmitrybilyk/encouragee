package com.encouragee.rabbit.configuration;

import com.encouragee.messaging.Receiver;
import com.encouragee.rabbit.SimpleMessage;
import com.encouragee.rabbit.model.Conversation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
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
    public static final String fanoutExchangeName = "fanout-exchange";
    public static final String headersExchangeName = "headers-exchange";
    public static final String COM_ENCOURAGEE_MESSAGING_QUEUE_TOPIC_1 = "com.encouragee.messaging.queueTopic1";
    public static final String COM_ENCOURAGEE_MESSAGING_QUEUE_TOPIC_2 = "com.encouragee.messaging.queueTopic2";
    public static final String COM_ENCOURAGEE_MESSAGING_QUEUE_TOPIC_3 = "com.encouragee.messaging.queueTopic3";
    public static final String FANOUT_QUEUE_1 = "some.fanout.queue";
    public static final String FANOUT_QUEUE_2 = "some2.fanout2.queue2";
    public static final String HEADERS_QUEUE_1 = "headers.queue.1";
    public static final String HEADERS_QUEUE_2 = "headers.queue.2";


//    Direct exchange ---------------------------------------------------------------------------------------------------
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
// End of Topic Exchange ------------------------------------------------------------------------------------------------

//    Fanout Exchange ---------------------------------------------------------------------------------------------------

    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(fanoutExchangeName);
    }

    @Bean
    Queue queueFanout1() {
        return new Queue(FANOUT_QUEUE_1, false);
    }

    @Bean
    Queue queueFanout2() {
        return new Queue(FANOUT_QUEUE_2, false);
    }

    @Bean
    Binding fanoutBinding1(Queue queueFanout1, FanoutExchange fanoutExchange){
        return bind(queueFanout1).to(fanoutExchange);
    }

    @Bean
    Binding fanoutBinding2(Queue queueFanout2, FanoutExchange fanoutExchange){
        return bind(queueFanout2).to(fanoutExchange);
    }

    @RabbitListener(queues = { FANOUT_QUEUE_1 })
    public void receiveMessageFromFanoutQueue1(String message) {
        System.out.println("Received direct message (" + FANOUT_QUEUE_1 + ") message: " + message);
    }

    @RabbitListener(queues = { FANOUT_QUEUE_2 })
    public void receiveMessageFromFanoutQueue2(String message) {
        System.out.println("Received direct message (" + FANOUT_QUEUE_2 + ") message: " + message);
    }

//    Fanout end --------------------------------------------------------------------------------------------------------

//    Headers exchange --------------------------------------------------------------------------------------------------


    @Bean
    HeadersExchange headersExchange() {
        return new HeadersExchange(headersExchangeName);
    }

    @Bean
    Queue queueHeaders1() {
        return new Queue(HEADERS_QUEUE_1, false);
    }

    @Bean
    Queue queueHeaders2() {
        return new Queue(HEADERS_QUEUE_2, false);
    }

    @Bean
    Binding headersBinding1(Queue queueHeaders1, HeadersExchange headersExchange){
        return BindingBuilder.bind(queueHeaders1).to(headersExchange)
                .where("key1").matches("value1");
    }

    @Bean
    Binding headersBinding11(Queue queueHeaders1, HeadersExchange headersExchange){
        return BindingBuilder.bind(queueHeaders1).to(headersExchange)
                .where("key11").matches("value11");
    }

    @Bean
    Binding headersBinding111(Queue queueHeaders1, HeadersExchange headersExchange){
        return BindingBuilder.bind(queueHeaders1).to(headersExchange)
                .where("key111").matches("value111");
    }

    @Bean
    Binding headersBinding2(Queue queueHeaders2, HeadersExchange headersExchange){
        return BindingBuilder.bind(queueHeaders2).to(headersExchange).where("key2").matches("value2");
    }

    @RabbitListener(queues = { HEADERS_QUEUE_1 })
    public void receiveMessageFromHeadersQueue1(String message) {
        System.out.println("Received direct message (" + HEADERS_QUEUE_1 + ") message: " + message);
    }

    @RabbitListener(queues = { HEADERS_QUEUE_2 })
    public void receiveMessageFromHeadersQueue2(String message) {
        System.out.println("Received direct message (" + HEADERS_QUEUE_2 + ") message: " + message);
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

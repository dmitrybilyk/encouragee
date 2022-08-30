package com.encouragee;

import com.encouragee.messaging.Receiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.springframework.amqp.core.BindingBuilder.bind;

@Configuration
@ComponentScan(basePackages = {"com.encouragee.controller", "com.encouragee.messaging", "com.encouragee.camel",
        "com.encouragee.rabbit.controller"})
public class RabbitMQConfiguration {
    public static final String queueNameForDirectExchange = "queue.for.direct.exchange";
    public static final String topicExchangeName = "spring-boot-exchange";

    @RabbitListener(queues = queueNameForDirectExchange)

//    @Bean
//    TopicExchange exchange() {
//        return new TopicExchange(topicExchangeName);
//    }


    @Bean
    Queue queue() {
        return new Queue(queueNameForDirectExchange, false);
    }
//
//    @Bean
//    Binding binding(Queue queue, TopicExchange exchange){
//        return bind(queue).to(exchange).with("com.encouragee.messaging.#");
//    }

    @RabbitListener(queues = { queueNameForDirectExchange })
    public void receiveMessageFromDirect(String message) {
        System.out.println("Received topic 2 (" + queueNameForDirectExchange + ") message: " + message);
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

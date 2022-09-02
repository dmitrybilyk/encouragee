package com.encouragee.rabbit.controller;

import com.encouragee.rabbit.model.Conversation;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.encouragee.EncourageeApplication.*;
import static com.encouragee.rabbit.configuration.RabbitMQConfiguration.*;

@RestController("/rabbit")
public class RabbitRestController {

    private static String ROUTING_KEY_USER_IMPORTANT_WARN = "user.important.warn";
    private static String ROUTING_KEY_USER_IMPORTANT_ERROR = "user.important.error";

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @GetMapping("/send/direct/{value}")
    public String sendToDirectExchange(@PathVariable String value) {
        rabbitTemplate.convertAndSend(queueNameForDirectExchange, value);
        return "message sent to direct exchange";
    }

    @GetMapping("/sendToTopicExchangeFirst")
    public String sendToTopicExchangeFirst() {
        Conversation conversation = new Conversation();
        conversation.setName("my conversation first queue");
        rabbitTemplate.convertAndSend(topicExchangeName, "com.encouragee.messaging.first.conversation",
                conversation);
        return "conversation sent to topic exchange";
    }

    @GetMapping("/sendToTopicExchangeSecond")
    public String sendToTopicExchangeSecond() {
        Conversation conversation = new Conversation();
        conversation.setName("my conversation second queue");
        rabbitTemplate.convertAndSend(topicExchangeName, "com.encouragee.messaging.multi.second.conversation",
                conversation);
        return "conversation sent to topic exchange";
    }

    @GetMapping("/sendToTopicExchangeMulti")
    public String sendToTopicExchangeMulti() {
        Conversation conversation = new Conversation();
        conversation.setName("my conversation second queue");
        rabbitTemplate.convertAndSend(topicExchangeName, "com.encouragee.messaging.multi.hello",
                conversation);
        return "conversation sent to topic exchange";
    }

    @GetMapping("/sendFanout")
    public String sendFanout() {
        String message = " payload is broadcast";
        rabbitTemplate.convertAndSend(fanoutExchangeName, "routing.does.not.matter", "fanout" + message);
        return "fanout sent";
    }

    @GetMapping("/sendHeaders")
    public String sendHeaders() {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("key1", "value1");
        messageProperties.setHeader("key11", "value11");
        messageProperties.setHeader("key111", "value111777");
        messageProperties.setHeader("x-match", "all");
        MessageConverter messageConverter = new SimpleMessageConverter();
        Message message = messageConverter.toMessage("some message data", messageProperties);
        rabbitTemplate.send(headersExchangeName, "", message);

        return "Message sent to the RabbitMQ Header Exchange Successfully";
    }

}

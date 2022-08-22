package com.encouragee.rabbit.controller;

import com.encouragee.rabbit.model.Conversation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.encouragee.EncourageeApplication.*;

@RestController("/rabbit")
public class RabbitRestController {

    private static String ROUTING_KEY_USER_IMPORTANT_WARN = "user.important.warn";
    private static String ROUTING_KEY_USER_IMPORTANT_ERROR = "user.important.error";

    @Autowired
    private RabbitTemplate rabbitTemplate;
//    private RabbitTemplate myRabbitTemplate;

    @GetMapping("/send/{value}")
    public String saveProduct(@PathVariable String value) {
        rabbitTemplate.convertAndSend(queueName, value);
        return "message sent";
    }

    @GetMapping("/sendFanout")
    public String sendFanout() {
        String message = " payload is broadcast";
        rabbitTemplate.convertAndSend(FANOUT_EXCHANGE_NAME, "", "fanout" + message);
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE_NAME, ROUTING_KEY_USER_IMPORTANT_WARN, "topic important warn" + message);
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE_NAME, ROUTING_KEY_USER_IMPORTANT_ERROR, "topic important error" + message);
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE_NAME, ROUTING_KEY_USER_IMPORTANT_ERROR, message + "topic important error");
        return "fanout sent";
    }

    @GetMapping("/sendConversation")
    public String sendConversation() {
        Conversation conversation = new Conversation();
        conversation.setName("my conversation");
        rabbitTemplate.convertAndSend(topicConversationExchange, "conversation-routing-key",
                conversation);
        return "conversation sent";
    }



}

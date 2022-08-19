package com.encouragee.rabbit.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.encouragee.EncourageeApplication.queueName;

@RestController("/rabbit")
public class RabbitRestController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/send/{value}")
    public String saveProduct(@PathVariable String value) {
        rabbitTemplate.convertAndSend(queueName, value);
        return "message sent";
    }

}

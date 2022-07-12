package com.encouragee.messaging;

import org.springframework.stereotype.Component;

@Component
public class Receiver {

    public void receiveMessage(String message) {
        System.out.println(message);
    }
}

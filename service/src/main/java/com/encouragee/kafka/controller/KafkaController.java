//package com.encouragee.kafka.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.util.concurrent.ListenableFuture;
//import org.springframework.util.concurrent.ListenableFutureCallback;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController(value = "/kafka")
//public class KafkaController {
//
//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    @GetMapping(value = "/send/message")
//    public void sendKafkaMessage() {
//        sendMessage("my kafka message");
//    }
//
//    public void sendMessage(String message) {
//
//        ListenableFuture<SendResult<String, String>> future =
//                kafkaTemplate.send("topicName", message);
//
//        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
//
//            @Override
//            public void onSuccess(SendResult<String, String> result) {
//                System.out.println("Sent message=[" + message +
//                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
//            }
//            @Override
//            public void onFailure(Throwable ex) {
//                System.out.println("Unable to send message=["
//                        + message + "] due to : " + ex.getMessage());
//            }
//        });
//    }
//}

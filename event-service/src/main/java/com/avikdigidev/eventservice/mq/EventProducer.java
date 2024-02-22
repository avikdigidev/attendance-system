package com.avikdigidev.eventservice.mq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {
    @Value("${app.kafka.topic.swipe-in}")
    private String swipeInTopic;
    @Value("${app.kafka.topic.swipe-out}")
    private String swipeOutTopic;


    private final KafkaTemplate<String, String> kafkaTemplate;

    public EventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendSwipeInEvent(String employeeId) {
        // Create and send swipe in event to Kafka topic
        String message = "Swipe in event for employee: " + employeeId;
        kafkaTemplate.send(swipeInTopic, message);
    }

    public void sendSwipeOutEvent(String employeeId) {
        // Create and send swipe out event to Kafka topic
        String message = "Swipe out event for employee: " + employeeId;
        kafkaTemplate.send(swipeOutTopic, message);
    }
}

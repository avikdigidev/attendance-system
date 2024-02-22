package com.avikdigidev.eventservice.mq;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public EventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendSwipeInEvent(String employeeId) {
        // Create and send swipe in event to Kafka topic
        String message = "Swipe in event for employee: " + employeeId;
        kafkaTemplate.send("swipe-in-topic", message);
    }

    public void sendSwipeOutEvent(String employeeId) {
        // Create and send swipe out event to Kafka topic
        String message = "Swipe out event for employee: " + employeeId;
        kafkaTemplate.send("swipe-out-topic", message);
    }
}

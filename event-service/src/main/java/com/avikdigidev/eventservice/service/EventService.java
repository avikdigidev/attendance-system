package com.avikdigidev.eventservice.service;

import org.springframework.kafka.annotation.*;
import org.springframework.stereotype.*;

@Service
public class EventService {

    @KafkaListener(topics = "swipe-in-topic")
    public void handleSwipeInEvent(String message) {
        // Process swipe in event received from Kafka
        System.out.println("Received swipe in event: " + message);
    }

    @KafkaListener(topics = "swipe-out-topic")
    public void handleSwipeOutEvent(String message) {
        // Process swipe out event received from Kafka
        System.out.println("Received swipe out event: " + message);
    }
}

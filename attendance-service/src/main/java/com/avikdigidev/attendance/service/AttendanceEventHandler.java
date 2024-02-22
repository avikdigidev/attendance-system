package com.avikdigidev.attendance.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.avikdigidev.attendance.constants.AttendanceConstants.*;

@Component
public class AttendanceEventHandler {

    @KafkaListener(topics = SWIPE_IN_TOPIC, groupId = ATTENDANCE_GROUP_ID)
    public void listenSwipeInEvent(String message) {
        // Logic to handle swipe in event
    }

    @KafkaListener(topics = SWIPE_OUT_TOPIC, groupId = ATTENDANCE_GROUP_ID)
    public void listenSwipeOutEvent(String message) {
        // Logic to handle swipe out event
    }

    // Other methods for event handling and attendance calculation
}

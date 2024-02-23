package com.avikdigidev.eventservice.service;

import com.avikdigidev.eventservice.model.Event;
import com.avikdigidev.eventservice.model.SwipeEvent;
import com.avikdigidev.eventservice.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.*;
import org.springframework.stereotype.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {



    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event createEvent(SwipeEvent event) {
        return eventRepository.save(event);
    }

    public void swipeIn(String employeeId) {
        // Business logic for swipe in operation
        eventRepository.save(AttendanceRecord.builder().employeeId(employeeId).swipeInTimestamp(LocalDateTime.now()).build());
        kafkaTemplate.send(SWIPE_IN_TOPIC, employeeId);
    }

    public void swipeOut(String employeeId) throws NoDataFoundException {
        // Validate employeeId, amd if he checked in or not
        List<AttendanceRecord> attendanceRecords = eventRepository.findFirstByEmployeeIdAndDateOrderBySwipeInTimestampAsc(employeeId, LocalDateTime.now().toLocalDate());

        if (!attendanceRecords.isEmpty()) {
            AttendanceRecord attendanceRecord = attendanceRecords.getFirst();
            attendanceRecord.setSwipeOutTimestamp(LocalDateTime.now());
            eventRepository.save(attendanceRecord);
            // Publish swipe out event to Kafka
            kafkaTemplate.send(SWIPE_OUT_TOPIC, employeeId);
        } else {
            throw new NoDataFoundException("No swipe in record found for employee: " + employeeId, "EMP_NF_001");
        }
    }
}

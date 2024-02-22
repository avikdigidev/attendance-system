package com.avikdigidev.eventservice.service;

import com.avikdigidev.eventservice.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.*;
import org.springframework.stereotype.*;

import java.util.List;

@Service
public class EventService {



    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }
}

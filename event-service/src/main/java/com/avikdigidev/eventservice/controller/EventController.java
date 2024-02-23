package com.avikdigidev.eventservice.controller;

import com.avikdigidev.eventservice.model.Event;
import com.avikdigidev.eventservice.model.SwipeEvent;
import com.avikdigidev.eventservice.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody SwipeEvent swipeEvent) {
        Event createdEvent = eventService.createEvent(swipeEvent);
        return ResponseEntity.ok(createdEvent);
    }

}

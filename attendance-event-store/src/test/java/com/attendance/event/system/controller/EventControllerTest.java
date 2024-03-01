package com.attendance.event.system.controller;

import com.attendance.event.system.dto.EventType;
import com.attendance.event.system.model.SwipeEvent;
import com.attendance.event.system.services.SwipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    @Mock
    private SwipeService swipeService;

    @InjectMocks
    private EventController eventController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    @Test
    public void testSwipeEvent_Success() throws Exception {
        SwipeEvent swipeEvent = new SwipeEvent();
        swipeEvent.setEmployeeId(123);
        swipeEvent.setEventTimestamp(Instant.now());

        when(swipeService.processSwipeEvent(any(SwipeEvent.class))).thenReturn("Swipe processed successfully");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/swipe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(swipeEvent);
    }
}
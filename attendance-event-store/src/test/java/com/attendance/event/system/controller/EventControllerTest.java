package com.attendance.event.system.controller;

import com.attendance.event.system.dto.EventType;
import com.attendance.event.system.model.SwipeEvent;
import com.attendance.event.system.services.SwipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    @MockBean
    private SwipeService swipeService;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(this).build();
    }


    @Test
    public void testSwipeEvent_Success() throws Exception {
        SwipeEvent swipeEvent = new SwipeEvent();
        swipeEvent.setEmployeeId(123);
        swipeEvent.setEventTimestamp(Instant.now());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(swipeService.processSwipeEvent(any(SwipeEvent.class))).thenReturn("Swipe processed successfully");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/swipe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(swipeEvent)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Swipe processed successfully"));

    }
}


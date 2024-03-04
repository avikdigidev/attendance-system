package com.attendance.event.store.system.controller;


import com.attendance.event.store.system.Controller.EventController;
import com.attendance.event.store.system.Services.AttendanceCalculatorService;
import com.attendance.event.store.system.Services.EventService;
import com.attendance.event.store.system.model.SwipeEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    @MockBean
    private EventService eventService;

    @MockBean
    private AttendanceCalculatorService attendanceCalculatorService;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(this).build();
    }


    @Test
    public void testSwipeEvent_Success() throws Exception {
        SwipeEvent swipeEvent = new SwipeEvent();
        swipeEvent.setEmployeeId(100);
        swipeEvent.setEventTimestamp(Instant.now());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(eventService.processSwipeEvent(any(SwipeEvent.class))).thenReturn("Swipe event processed successfully");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/swipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(swipeEvent)))
                .andExpect(status().isOk())
                .andExpect(content().string("Swipe event processed successfully"));
    }

    @Test
    public void testCalculateEmployeeAttendance() throws Exception {
        int mockRecords = 10;
        when(attendanceCalculatorService.calculateTotalHoursBySwipeInAndOutDate()).thenReturn(mockRecords);

        mockMvc.perform(get("/api/calculateAttendance"))
                .andExpect(status().isOk())
                .andExpect(content().string("10 records updated"));
    }

}


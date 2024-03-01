package com.attendance.calculation.system.controller;

import com.attendance.calculation.system.services.AttendanceCalculatorService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AttendanceController.class)
public class AttendanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AttendanceCalculatorService attendanceService;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(this).build();
    }

    @Test
    public void testCalculateEmployeeAttendance() throws Exception {
        int mockRecords = 10;
        when(attendanceService.calculateTotalHoursBySwipeInAndOutDate()).thenReturn(mockRecords);

        mockMvc.perform(get("/api/calculateAttendance"))
                .andExpect(status().isOk())
                .andExpect(content().string("10 records updated"));
    }
}

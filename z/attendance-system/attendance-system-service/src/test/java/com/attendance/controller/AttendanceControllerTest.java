package com.attendance.controller;


import com.attendance.dto.Employee;
import com.attendance.exception.ApiError;
import com.attendance.exception.CustomException;
import com.attendance.service.AttendanceService;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import com.attendance.dto.EmployeeDetails;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;

@SpringBootTest
@AutoConfigureMockMvc
public class AttendanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AttendanceService attendanceService;

    @InjectMocks
    private AttendanceController attendanceController;

    @BeforeEach
    void setUp() {
         MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAttendance() throws Exception {

        int employeeId = 100;
        EmployeeDetails employeeDetails = new EmployeeDetails();
        Employee employee = new Employee();
        employee.setEmployeeId(100);
        employee.setAttendanceStatus("Absent");
        employee.setTotalTime("12:00");
        employee.setDate(new Date(System.currentTimeMillis()));
        employeeDetails.setEmployee(Collections.singletonList(employee));

        when(attendanceService.getEmployeeList(anyInt())).thenReturn(employeeDetails);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/attendance/getAttendance/{employeeId}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetAttendanceException() throws Exception {

        int employeeId = 1;
        EmployeeDetails employeeDetails = new EmployeeDetails();
        employeeDetails.setApiError(new ApiError(HttpStatus.OK, "not found", new CustomException("not found")));

        when(attendanceService.getEmployeeList(anyInt())).thenReturn(employeeDetails);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/attendance/getAttendance/{employeeId}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testGetAttendanceOfAllEmployee() throws Exception {

        EmployeeDetails employeeDetails = new EmployeeDetails();
        Employee employee = new Employee();
        employee.setEmployeeId(1);
        employee.setAttendanceStatus("Absent");
        employee.setTotalTime("12:00");
        employee.setDate(new Date(System.currentTimeMillis()));

        Employee employee2 = new Employee();
        employee2.setEmployeeId(2);
        employee2.setAttendanceStatus("Present");
        employee2.setTotalTime("12:00");
        employee2.setDate(new Date(System.currentTimeMillis()));
        employeeDetails.setEmployee(Arrays.asList(employee, employee2));

        when(attendanceService.getEmployeeList()).thenReturn(employeeDetails);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/attendance/allEmployees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
//                .andExpect(MockMvcResultMatchers.jsonPath("$.employeeList").exists())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.employeeList[0].employeeId").value(employee.getEmployeeId()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.employeeList[0].attendanceStatus").value(employee.getAttendanceStatus()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.employeeList[0].totalTime").value(employee.getTotalTime()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.employeeList[0].date").value(employee.getDate().toString()))
    }
}



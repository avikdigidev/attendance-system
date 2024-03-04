package com.attendance.services;

import com.attendance.dto.EmployeeDetails;
import com.attendance.entity.Attendance;
import com.attendance.exception.ApiError;
import com.attendance.exception.CustomException;
import com.attendance.repository.AttendanceRepository;
import com.attendance.service.AttendanceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private AttendanceService attendanceService;


    @Test
    public void getEmployeeList() {

        int employeeId = 1;
        List<Attendance> attendanceList = new ArrayList<>();
        Attendance attendance = new Attendance();
        attendance.setAttendanceId("1");
        attendance.setEmployeeId(1);
        attendance.setAttendanceStatus("Present");
        attendance.setTotalTime("12:00");
        attendance.setDate(new Date(System.currentTimeMillis()));
        attendanceList.add(attendance);
        when(attendanceRepository.findAllByEmployeeId(employeeId)).thenReturn(attendanceList);

        EmployeeDetails result = attendanceService.getEmployeeList(employeeId);

        assertAll(
                () -> assertEquals(1, result.getEmployee().get(0).getEmployeeId()),
                () -> assertEquals("Present", result.getEmployee().get(0).getAttendanceStatus()),
                () -> assertNotNull(result.getEmployee().get(0).getDate()),
                () -> assertEquals("12:00", result.getEmployee().get(0).getTotalTime()));
        verify(attendanceRepository).findAllByEmployeeId(employeeId);
    }

    @Test
    public void getAllEmployeeList() {

        List<Attendance> attendanceList = new ArrayList<>();
        Attendance attendance = new Attendance();
        attendance.setAttendanceId("1");
        attendance.setEmployeeId(1);
        attendance.setAttendanceStatus("Present");
        attendance.setTotalTime("12:00");
        attendance.setDate(new Date(System.currentTimeMillis()));

        Attendance attendance2 = new Attendance();
        attendance2.setAttendanceId("2");
        attendance2.setEmployeeId(2);
        attendance2.setAttendanceStatus("Present");
        attendance2.setTotalTime("10:00");
        attendance2.setDate(new Date(System.currentTimeMillis()));

        attendanceList.add(attendance);
        attendanceList.add(attendance2);
        when(attendanceRepository.findAll()).thenReturn(attendanceList);

        EmployeeDetails result = attendanceService.getEmployeeList();

        assertAll(
                () -> assertEquals(1, result.getEmployee().get(0).getEmployeeId()),
                () -> assertEquals(2, result.getEmployee().get(1).getEmployeeId()),
                () -> assertEquals("Present", result.getEmployee().get(0).getAttendanceStatus()),
                () -> assertNotNull(result.getEmployee().get(0).getDate()),
                () -> assertEquals("12:00", result.getEmployee().get(0).getTotalTime()));
        verify(attendanceRepository).findAll();
    }

    @Test
    public void getAllEmployeeListException() {

        EmployeeDetails employeeDetails = new EmployeeDetails();
        employeeDetails.setApiError(new ApiError(HttpStatus.OK, "not found", new CustomException("not found")));

        when(attendanceRepository.findAll()).thenThrow(new RuntimeException("error"));

        EmployeeDetails result = attendanceService.getEmployeeList();

        verify(attendanceRepository).findAll();
    }

}

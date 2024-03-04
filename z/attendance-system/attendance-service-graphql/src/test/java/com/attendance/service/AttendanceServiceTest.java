package com.attendance.service;

import com.attendance.dto.Attendance;
import com.attendance.dto.AttendanceRecord;
import com.attendance.proxies.AttendanceSystemServiceProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AttendanceServiceTest {

    @Mock
    private AttendanceSystemServiceProxy attendanceSystemServiceProxy;
    @InjectMocks
    private AttendanceService attendanceService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetAttendanceData() {

        int employeeId = 123;
        List<Attendance> expectedAttendances = new ArrayList<>();
        expectedAttendances.add(new Attendance());
        when(attendanceSystemServiceProxy.retrieveExchangeValue(employeeId)).thenReturn(expectedAttendances);

        AttendanceRecord actualAttendances = attendanceService.getAttendanceData(employeeId);

        assertEquals(expectedAttendances, actualAttendances.getAttendanceList());
    }
}

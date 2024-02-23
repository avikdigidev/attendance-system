package com.avikdigidev.attendance.controller;

import com.avikdigidev.attendance.dto.response.EmployeeStatusResponse;
import com.avikdigidev.attendance.service.AttendanceService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@DgsComponent
public class AttendanceDataFetcher {

    @Autowired
    private AttendanceService attendanceService;


    @DgsQuery
    public ResponseEntity<EmployeeStatusResponse> getAttendanceStatus(@InputArgument String employeeId) {
        try {
            EmployeeStatusResponse totalHours = attendanceService.getAttendanceStatus(employeeId);
            return ResponseEntity.ok(totalHours);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

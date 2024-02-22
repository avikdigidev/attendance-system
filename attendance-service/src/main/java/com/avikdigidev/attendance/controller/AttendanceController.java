package com.avikdigidev.attendance.controller;

import com.avikdigidev.attendance.dto.response.EmployeeStatusResponse;
import com.avikdigidev.attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {


    @Autowired
    private AttendanceService attendanceService;

    @QueryMapping
    public ResponseEntity<EmployeeStatusResponse> getAttendanceStatus(@Argument String employeeId) {
        try {
            EmployeeStatusResponse totalHours = attendanceService.getAttendanceStatus(employeeId);
            return ResponseEntity.ok(totalHours);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}

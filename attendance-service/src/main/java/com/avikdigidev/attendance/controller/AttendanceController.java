package com.avikdigidev.attendance.controller;

import com.avikdigidev.attendance.dto.response.AttendanceInfoResponse;
import com.avikdigidev.attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {


    @Autowired
    private AttendanceService attendanceService;

    @QueryMapping
    public ResponseEntity<Double> getTotalHours(@PathVariable Long employeeId) {
        try {
            double totalHours = attendanceService.getTotalHours(employeeId);
            return ResponseEntity.ok(totalHours);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @QueryMapping
    public ResponseEntity<AttendanceInfoResponse> attendanceInfo(String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        List<String> absentees = attendanceService.getAbsentees(parsedDate);
        List<String> halfDayWorkers = attendanceService.getHalfDayWorkers(parsedDate);
        List<String> fullDayWorkers = attendanceService.getFullDayWorkers(parsedDate);
        return ResponseEntity.ok(new AttendanceInfoResponse(absentees, halfDayWorkers, fullDayWorkers));
    }


}

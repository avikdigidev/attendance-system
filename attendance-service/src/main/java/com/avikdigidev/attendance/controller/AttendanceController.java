package com.avikdigidev.attendance.controller;

import com.avikdigidev.attendance.dto.request.*;
import com.avikdigidev.attendance.service.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/swipe/in")
    public ResponseEntity<String> swipeIn(@RequestBody SwipeRequest request) {
        attendanceService.swipeIn(request.getEmployeeId());
        return ResponseEntity.ok("Swipe in recorded successfully.");
    }

    @PostMapping("/swipe/out")
    public ResponseEntity<String> swipeOut(@RequestBody SwipeRequest request) {
        attendanceService.swipeOut(request.getEmployeeId());
        return ResponseEntity.ok("Swipe out recorded successfully.");
    }
}

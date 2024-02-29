package com.attendance.calculation.system.controller;

import com.attendance.calculation.system.Services.AttendanceCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class AttendanceController {


    @Autowired
	private AttendanceCalculatorService attendanceService;


    
	@GetMapping("/calculateAttendance")
	private ResponseEntity<String> calculateEmployeeAttendance() {
		Integer records = attendanceService.calculateTotalHoursBySwipeInAndOutDate();
		return ResponseEntity.ok(records + " records updated");
	}
}

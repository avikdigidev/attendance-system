package com.attendance.domain.controller;

import com.attendance.domain.dto.Employee;
import com.attendance.domain.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class AttendanceController {
	
	@Autowired
	private AttendanceService attendanceService;

	@GetMapping("/getAttendance/{employeeId}")
	public ResponseEntity<List<Employee>> getAttandance(@PathVariable("employeeId") int employeeId) {
		
		List<Employee> employeeList = attendanceService.getEmployeeList(employeeId);
		
		return ResponseEntity.ok(employeeList);
	}

}

package com.attendance.service;

import java.util.List;
import java.util.stream.Collectors;

import com.attendance.dto.EmployeeDetails;
import com.attendance.exception.ApiError;
import com.attendance.exception.CustomException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.attendance.dto.Employee;
import com.attendance.entity.Attendance;
import com.attendance.repository.AttendanceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AttendanceService {

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private ObjectMapper objectMapper;

	public EmployeeDetails getEmployeeList(int employeeId) {

		EmployeeDetails employeeDetails = new EmployeeDetails();
		try {
			List<Attendance> attendanceList = attendanceRepository.findAllByEmployeeId(employeeId);
			if (!attendanceList.isEmpty()) {
				List<Employee> employeeList = attendanceList.stream().map(attendance -> {
					Employee employee = new Employee();
					BeanUtils.copyProperties(attendance, employee);
					return employee;
				}).collect(Collectors.toList());
				employeeDetails.setEmployee(employeeList);
			} else {
				log.info("No details found");
				throw new CustomException("No details found");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomException(e.getMessage());
		}
		return employeeDetails;
	}

	public EmployeeDetails getEmployeeList() {

		EmployeeDetails employeeDetails = new EmployeeDetails();
		try {
			List<Attendance> attendanceList = attendanceRepository.findAll();
			if (!attendanceList.isEmpty()) {
				 List<Employee> employeeList = attendanceList.stream().map(attendance -> {
					Employee employee = new Employee();
					BeanUtils.copyProperties(attendance, employee);
					return employee;
				}).collect(Collectors.toList());
				 employeeDetails.setEmployee(employeeList);
			} else {
				log.info("No details found");
				throw new CustomException("No details found");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			employeeDetails.setApiError(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e));
		}
		return employeeDetails;
	}

	@KafkaListener(topics = "employee-attendance-topic")
	public void listenAttendance(String message) {
		log.info("message received : " + message);
		try {
			Attendance attendance = objectMapper.readValue(message, Attendance.class);
			attendanceRepository.save(attendance);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
		}
	}
}

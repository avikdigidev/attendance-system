package com.attendance.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import com.attendance.domain.entity.Attendance;
import com.attendance.domain.repository.AttendanceRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attendance.domain.dto.Employee;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AttendanceService {

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private ObjectMapper objectMapper;

	public List<Employee> getEmployeeList(int employeeId) {

		try {
			List<Attendance> attendanceList = attendanceRepository.findAllByEmployeeId(employeeId);
			if (!attendanceList.isEmpty()) {
				return attendanceList.stream().map(attendance -> {
					Employee employee = new Employee();
					BeanUtils.copyProperties(attendance, employee);
					return employee;
				}).collect(Collectors.toList());
			} else {
				log.info("No details found");
				throw new RuntimeException("No details found");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

}

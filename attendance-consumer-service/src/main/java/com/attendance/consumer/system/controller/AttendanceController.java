package com.attendance.consumer.system.controller;


import com.attendance.consumer.system.entity.Attendance;
import com.attendance.consumer.system.repository.AttendanceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AttendanceController {

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private ObjectMapper objectMapper;

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

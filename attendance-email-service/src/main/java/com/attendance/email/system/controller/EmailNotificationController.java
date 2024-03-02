package com.attendance.email.system.controller;


import com.attendance.email.system.dto.AttendanceEmail;
import com.attendance.email.system.service.AttendanceEmailNotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class EmailNotificationController {

	@Autowired
	private AttendanceEmailNotificationService attendanceEmailNotificationService;

	@Autowired
	private ObjectMapper objectMapper;

	@KafkaListener(topics = "absent-email-event")
	public void listenAttendance(String message) {
		log.info("message received : " + message);
		try {
			AttendanceEmail notification = objectMapper.readValue(message, AttendanceEmail.class);
			attendanceEmailNotificationService.sendEmail(notification);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
		}

	}
}

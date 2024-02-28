package com.attendance.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.attendance.dto.Attendance;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AttendanceService {

//	@Autowired
//	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${attendance.service.host}")
	private String attendanceServiceHost;

	@Value("${attendance.service.endpoint}")
	private String attendanceServiceEndpoint;

	public List<Attendance> getAttendanceData(long employeeId) {
		List<Attendance> attendanceList = new ArrayList<>();
		RestTemplate restTemplate = new RestTemplate();
		try {
			
			final String url = attendanceServiceHost + attendanceServiceEndpoint;
			log.info("attendance service url: " + url);
			ResponseEntity<String> responseEntity = restTemplate
					.getForEntity(url, String.class, employeeId);

			if (responseEntity.hasBody()) {
				 attendanceList = objectMapper.readValue(responseEntity.getBody(),
						new TypeReference<List<Attendance>>() {
						});
				 log.info("attendance list " + attendanceList);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}

		return attendanceList;
	}

}

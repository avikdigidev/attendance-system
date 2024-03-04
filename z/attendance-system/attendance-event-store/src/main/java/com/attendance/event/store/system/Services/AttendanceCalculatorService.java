package com.attendance.event.store.system.Services;

import com.attendance.event.store.system.exception.EntityNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.attendance.event.store.system.Repository.SwipeRepository;
import com.attendance.event.store.system.constants.AppConstants;
import com.attendance.event.store.system.dto.EventDetail;
import com.attendance.event.store.system.model.EmployeeAttendance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class AttendanceCalculatorService {

	@Autowired
	private SwipeRepository swipeRepository;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	/*
	* this method first query the db and get the records Of each employee by first swipe in and last swipe out.
	* then perform the calculation to check the status of employee and send that data to kafka.
	* */
	public int calculateTotalHoursBySwipeInAndOutDate() {
		Instant startDate = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant();
		Instant endDate = startDate.atZone(ZoneOffset.UTC).plusDays(1).toInstant();
		List<EventDetail> firstSwipeInByEmployeeAndDate = swipeRepository.findFirstSwipeInByEmployeeAndDate(startDate,
				endDate);
		if(firstSwipeInByEmployeeAndDate.isEmpty())
			throw  new EntityNotFoundException("not found");
		List<EventDetail> lastSwipeOutByEmployeeAndDate = swipeRepository.findLastSwipeOutByEmployeeAndDate(startDate,
				endDate);
		if(lastSwipeOutByEmployeeAndDate.isEmpty())
			throw  new EntityNotFoundException("not found");
		log.info("calculation :: " + firstSwipeInByEmployeeAndDate + firstSwipeInByEmployeeAndDate);
		return calculateAttendance(firstSwipeInByEmployeeAndDate, lastSwipeOutByEmployeeAndDate);
	}

	/*
	* this method is calculating the attendance of employee by first swipe in and last swipe out logic
	* */
	public int calculateAttendance(List<EventDetail> firstSwipeInByEmployeeAndDate,
			List<EventDetail> lastSwipeOutByEmployeeAndDate) {

		Map<Integer, List<Instant>> map = new HashMap<>();
		AtomicInteger sendRecordCount = new AtomicInteger(0);

		Map<Integer, String> emailMapping = firstSwipeInByEmployeeAndDate.stream()
				.collect(Collectors.toMap(EventDetail::getEmployeeid, EventDetail::getEmailid));

		map = Stream.concat(firstSwipeInByEmployeeAndDate.stream(), lastSwipeOutByEmployeeAndDate.stream())
				.collect(Collectors.groupingBy(EventDetail::getEmployeeid,
						Collectors.mapping(EventDetail::getTimestamp, Collectors.toList())));

		map.entrySet().stream().forEach(entry -> {
			extracted(entry, emailMapping, sendRecordCount);
		});
		return sendRecordCount.get();
	}

	private void extracted(Map.Entry<Integer, List<Instant>> entry, Map<Integer, String> emailMapping, AtomicInteger sendRecordCount) {
		EmployeeAttendance employeeAttendance = new EmployeeAttendance();

		Instant firstTimestamp = !entry.getValue().isEmpty() ? entry.getValue().get(0) : null;
		Instant secondTimestamp = entry.getValue().size() > 1 ? entry.getValue().get(1) : null;
		Integer employeeId = entry.getKey();
		employeeAttendance.setEmployeeId(employeeId);
		employeeAttendance.setEmailId(emailMapping.getOrDefault(employeeId, employeeId + AppConstants.EMAIL_DOMAIN));
		if (firstTimestamp != null) {
			employeeAttendance.setDate(Date.valueOf(firstTimestamp.atZone(ZoneOffset.UTC).toLocalDate()));
		}
		setAttendance(firstTimestamp, secondTimestamp, employeeAttendance);
		sendEmployeeAttendanceEvent(employeeAttendance, sendRecordCount);
	}

	/*
	* this method will set the attendance data
	* */
	private void setAttendance(Instant startTime, Instant endTime, EmployeeAttendance employeeAttendance) {
		String attendanceStatus;
		String totalTime = "00:00";
		if (startTime != null && endTime != null) {
			Duration duration = Duration.between(startTime, endTime);
			if (duration.compareTo(Duration.ofHours(4)) < 0) {
				attendanceStatus = AppConstants.ABSENT;
			} else if (duration.compareTo(Duration.ofHours(8)) < 0) {
				attendanceStatus = AppConstants.HALF_DAY;
			} else {
				attendanceStatus = AppConstants.PRESENT;
			}
			long hours = duration.toHours();
			long minutes = duration.minusHours(hours).toMinutes();
			totalTime = String.format("%02d:%02d", hours, minutes);
		} else {
			// If either startTime or endTime is null, employee is absent
			attendanceStatus = AppConstants.ABSENT;
		}
		employeeAttendance.setAttendanceStatus(attendanceStatus);
		employeeAttendance.setTotalTime(totalTime);
	}

	/*
	* this method will publish message(EmployeeAttendance Data) to kafka.
	* performing count post publishing the data to return in response.
	* */
	private void sendEmployeeAttendanceEvent(EmployeeAttendance employeeAttedance, AtomicInteger count) {
		try {
			kafkaTemplate.send("employee-attendance-topic", objectMapper.writeValueAsString(employeeAttedance));
			count.incrementAndGet();
		} catch (JsonProcessingException e) {
			log.error("Unable to publish attendance of " + employeeAttedance.getEmployeeId() + " on "
					+ employeeAttedance.getDate());
		}
	}
}

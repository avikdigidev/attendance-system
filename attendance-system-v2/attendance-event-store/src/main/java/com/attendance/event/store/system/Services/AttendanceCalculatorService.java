package com.attendance.event.store.system.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.attendance.event.store.system.Repository.SwipeRepository;
import com.attendance.event.store.system.constants.AppConstants;
import com.attendance.event.store.system.dto.EventDetail;
import com.attendance.event.store.system.model.EmployeeAttedance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

	public int calculateTotalHoursBySwipeInAndOutDate() {
		Instant startDate = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant();
		Instant endDate = startDate.atZone(ZoneOffset.UTC).plusDays(1).toInstant();
		List<EventDetail> firstSwipeInByEmployeeAndDate = swipeRepository.findFirstSwipeInByEmployeeAndDate(startDate,
				endDate);
		List<EventDetail> lastSwipeOutByEmployeeAndDate = swipeRepository.findLastSwipeOutByEmployeeAndDate(startDate,
				endDate);
		log.info("calculation :: " + firstSwipeInByEmployeeAndDate + firstSwipeInByEmployeeAndDate);
		return calculateAttendance(firstSwipeInByEmployeeAndDate, lastSwipeOutByEmployeeAndDate);
	}

	public int calculateAttendance(List<EventDetail> firstSwipeInByEmployeeAndDate,
			List<EventDetail> lastSwipeOutByEmployeeAndDate) {

		Map<Integer, List<Instant>> map = new HashMap<>();
		int sendRecordCount = 0;

		map = Stream.concat(firstSwipeInByEmployeeAndDate.stream(), lastSwipeOutByEmployeeAndDate.stream())
				.collect(Collectors.groupingBy(EventDetail::getEmployeeid,
						Collectors.mapping(EventDetail::getTimestamp, Collectors.toList())));

		map.entrySet().stream().forEach(entry -> {
			EmployeeAttedance employeeAttedance = new EmployeeAttedance();
			setAttendance(entry.getValue().get(0), entry.getValue().get(1), employeeAttedance);
			employeeAttedance.setEmployeeId(entry.getKey());
			employeeAttedance.setDate(null);
			sendEmployeeAttendanceEvent(employeeAttedance, sendRecordCount);
		});
		return sendRecordCount;
	}

	private void setAttendance(Instant startTime, Instant endTime, EmployeeAttedance employeeAttedance) {
		String attendance;
		Duration duration = Duration.between(startTime, endTime);
		if (duration.compareTo(Duration.ofHours(4)) < 0)
			attendance = AppConstants.ABSENT;
		else if (duration.compareTo(Duration.ofHours(8)) < 0)
			attendance = AppConstants.HALF_DAY;
		else
			attendance = AppConstants.PRESENT;
		long hours = duration.toHours();
		long minutes = duration.minusHours(hours).toMinutes();
		String totalTime = String.format("%02d:%02d", hours, minutes);
		employeeAttedance.setAttendanceStatus(attendance);
		employeeAttedance.setTotalTime(totalTime);
	}

	private void sendEmployeeAttendanceEvent(EmployeeAttedance employeeAttedance, int count) {
		try {
			kafkaTemplate.send("employee-attendance-topic", objectMapper.writeValueAsString(employeeAttedance));
			count++;
		} catch (JsonProcessingException e) {
			log.error("Unable to publish attendance of " + employeeAttedance.getEmployeeId() + " on "
					+ employeeAttedance.getDate());
		}
	}
}

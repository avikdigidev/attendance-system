package com.attendance.calculation.system.Services;

import com.attendance.calculation.system.model.EmployeeAttedance;
import com.attendance.calculation.system.Repository.EventRepository;
import com.attendance.calculation.system.constants.AppConstants;
import com.attendance.calculation.system.dto.EventDetail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class AttendanceCalculatorService {

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public Integer calculateTotalHoursBySwipeInAndOutDate() {
		Instant startDate = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant();
		Instant endDate = startDate.atZone(ZoneOffset.UTC).plusDays(1).toInstant();
		List<EventDetail> firstSwipeInByEmployeeAndDate = eventRepository.findFirstSwipeInByEmployeeAndDate(startDate,
				endDate);
		List<EventDetail> lastSwipeOutByEmployeeAndDate = eventRepository.findLastSwipeOutByEmployeeAndDate(startDate,
				endDate);
		log.info("calculation :: " + firstSwipeInByEmployeeAndDate + firstSwipeInByEmployeeAndDate);
		return calculateAttendance(firstSwipeInByEmployeeAndDate, lastSwipeOutByEmployeeAndDate);
	}

	public int calculateAttendance(List<EventDetail> firstSwipeInByEmployeeAndDate,
			List<EventDetail> lastSwipeOutByEmployeeAndDate) {

		Map<Integer, List<Instant>> map;
		AtomicInteger sendRecordCount = new AtomicInteger(0);

		map = Stream.concat(firstSwipeInByEmployeeAndDate.stream(), lastSwipeOutByEmployeeAndDate.stream())
				.collect(Collectors.groupingBy(EventDetail::getEmployeeid,
						Collectors.mapping(EventDetail::getTimestamp, Collectors.toList())));

		map.entrySet().parallelStream().forEach(entry -> {
			EmployeeAttedance employeeAttedance = new EmployeeAttedance();
			setAttendance(entry.getValue().get(0), entry.getValue().get(1), employeeAttedance);
			employeeAttedance.setEmployeeId(entry.getKey());
			employeeAttedance.setDate(Date.valueOf(entry.getValue().get(0).atZone(ZoneOffset.UTC).toLocalDate()));
			sendEmployeeAttendanceEvent(employeeAttedance, sendRecordCount);
		});
		return sendRecordCount.get();
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

	private void sendEmployeeAttendanceEvent(EmployeeAttedance employeeAttedance, AtomicInteger count) {
		try {
			kafkaTemplate.send("employee-attendance-topic", objectMapper.writeValueAsString(employeeAttedance));
			count.incrementAndGet();
		} catch (JsonProcessingException e) {
			log.error("Unable to publish attendance of " + employeeAttedance.getEmployeeId() + " on "
					+ employeeAttedance.getDate());
		}
	}
}

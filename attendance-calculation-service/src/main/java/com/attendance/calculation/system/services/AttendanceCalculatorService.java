package com.attendance.calculation.system.services;

import com.attendance.calculation.system.dto.AttendanceEmail;
import com.attendance.calculation.system.model.Employee;
import com.attendance.calculation.system.repository.EventRepository;
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

    private static final String EMAIL_DOMAIN = "@gmail.com";
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
        Map<Integer, String> emailMapping = firstSwipeInByEmployeeAndDate.stream()
                .collect(Collectors.toMap(EventDetail::getEmployeeid, EventDetail::getEmailid));

        map = Stream.concat(firstSwipeInByEmployeeAndDate.stream(), lastSwipeOutByEmployeeAndDate.stream())
                .collect(Collectors.groupingBy(EventDetail::getEmployeeid,
                        Collectors.mapping(EventDetail::getTimestamp, Collectors.toList())));

        map.entrySet().parallelStream().forEach(entry -> {
            Employee employee = new Employee();
            Instant firstTimestamp = entry.getValue().size() > 0 ? entry.getValue().get(0) : null;
            Instant secondTimestamp = entry.getValue().size() > 1 ? entry.getValue().get(1) : null;
            Integer employeeId = entry.getKey();
            employee.setEmployeeId(employeeId);
            employee.setEmailId(emailMapping.getOrDefault(employeeId, employeeId + EMAIL_DOMAIN));
            if (firstTimestamp != null) {
                employee.setDate(Date.valueOf(firstTimestamp.atZone(ZoneOffset.UTC).toLocalDate()));
            }
            setAttendance(firstTimestamp, secondTimestamp, employee);
            publishEmployeeAttendanceEvent(employee, sendRecordCount);
        });
        return sendRecordCount.get();
    }

    private void setAttendance(Instant startTime, Instant endTime, Employee employee) {
        String attendance;
        String totalTime = "00:00";
        if (startTime != null && endTime != null) {
            Duration duration = Duration.between(startTime, endTime);
            if (duration.compareTo(Duration.ofHours(4)) < 0) {
                attendance = AppConstants.ABSENT;
                publishEmailEvent(employee, attendance);
            } else if (duration.compareTo(Duration.ofHours(8)) < 0) {
                attendance = AppConstants.HALF_DAY;
            } else {
                attendance = AppConstants.PRESENT;
            }
            long hours = duration.toHours();
            long minutes = duration.minusHours(hours).toMinutes();
            totalTime = String.format("%02d:%02d", hours, minutes);
            employee.setTotalTime(totalTime);
        } else {
            attendance = AppConstants.ABSENT;
            publishEmailEvent(employee, attendance);
        }
        employee.setAttendanceStatus(attendance);

    }

    private void publishEmailEvent(Employee employee, String attendance) {
        try {
            AttendanceEmail emailNotification = AttendanceEmail.builder()
                    .employee(employee)
                    .subject(attendance + "|NOTIFICATION|" + employee.getEmployeeId() + "-" + employee.getDate())
                    .body("                            You have been recorded as " + attendance + " ON " + employee.getDate() +
                            "                                    On this day your marked office hours are " + employee.getTotalTime())
                    .build();
            kafkaTemplate.send("absent-email-event", objectMapper.writeValueAsString(emailNotification));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void publishEmployeeAttendanceEvent(Employee employee, AtomicInteger count) {
        try {
            kafkaTemplate.send("employee-attendance-topic", objectMapper.writeValueAsString(employee));
            count.incrementAndGet();
        } catch (JsonProcessingException e) {
            log.error("Unable to publish attendance of " + employee.getEmployeeId() + " on "
                    + employee.getDate());
        }
    }
}

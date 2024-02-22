package com.avikdigidev.attendance.service;

import com.avikdigidev.attendance.constants.AttendanceConstants;
import com.avikdigidev.attendance.exceptions.NoDataFoundException;
import com.avikdigidev.attendance.model.AttendanceRecord;
import com.avikdigidev.attendance.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.avikdigidev.attendance.constants.AttendanceConstants.SWIPE_IN_TOPIC;
import static com.avikdigidev.attendance.constants.AttendanceConstants.SWIPE_OUT_TOPIC;


@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    public void swipeIn(String employeeId) {
        // Business logic for swipe in operation
        attendanceRepository.save(AttendanceRecord.builder().employeeId(employeeId).swipeInTimestamp(LocalDateTime.now()).build());
        kafkaTemplate.send(SWIPE_IN_TOPIC, employeeId);
    }

    public void swipeOut(String employeeId) throws NoDataFoundException {
        // Validate employeeId, amd if he checked in or not
        List<AttendanceRecord> attendanceRecords = attendanceRepository.findFirstByEmployeeIdAndDateOrderBySwipeInTimestampAsc(employeeId, LocalDateTime.now().toLocalDate());

        if (!attendanceRecords.isEmpty()) {
            AttendanceRecord attendanceRecord = attendanceRecords.getFirst();
            attendanceRecord.setSwipeOutTimestamp(LocalDateTime.now());
            attendanceRepository.save(attendanceRecord);
            // Publish swipe out event to Kafka
            kafkaTemplate.send(SWIPE_OUT_TOPIC, employeeId);
        } else {
            throw new NoDataFoundException("No swipe in record found for employee: " + employeeId, "EMP_NF_001");
        }
    }

    // Method to calculate end of day total hours and update attendance status
    public String calculateEndOfDayTotalHours(String employeeId) {
        // Retrieve all swipe-in records for the employee for the current day
        List<AttendanceRecord> swipeInRecords = attendanceRepository.findFirstByEmployeeIdAndDateOrderByTimestampDesc(employeeId, LocalDateTime.now().toLocalDate());

        if (swipeInRecords.isEmpty()) {
            return AttendanceConstants.ABSENT; // Employee didn't swipe in, so mark as absent
        }

        // Assuming the last swipe-in is considered as the start of the workday
        LocalDateTime startOfDay = swipeInRecords.get(0).getSwipeInTimestamp().toLocalDate().atStartOfDay();

        // Retrieve the last swipe-out record for the employee for the current day (if any)
        AttendanceRecord lastSwipeOutRecord = attendanceRepository.findFirstByEmployeeIdAndDateAndSwipeOutTimestampNotNullOrderBySwipeOutTimestampDesc(employeeId, LocalDateTime.now().toLocalDate());

        // If no swipe-out record found, use current time as the end of the workday
        LocalDateTime endOfDay = lastSwipeOutRecord != null ? lastSwipeOutRecord.getSwipeOutTimestamp() : LocalDateTime.now();

        // Calculate total hours worked
        Duration duration = Duration.between(startOfDay, endOfDay);
        long totalHours = duration.toHours();

        // Determine attendance status based on total hours
        String attendanceStatus;
        if (totalHours < 4) {
            attendanceStatus = AttendanceConstants.ABSENT;
        } else if (totalHours >= 4 && totalHours < 8) {
            attendanceStatus = "Half Day";
        } else {
            attendanceStatus = "Present";
        }

        return attendanceStatus;
    }

    // Method to calculate attendance status based on total hours worked
    private String calculateAttendanceStatus(long totalHours) {
        if (totalHours < 4) {
            return AttendanceConstants.ABSENT;
        } else if (totalHours >= 4 && totalHours < 8) {
            return "Half Day";
        } else {
            return "Present";
        }
    }
}

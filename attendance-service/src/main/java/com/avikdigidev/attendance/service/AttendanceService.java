package com.avikdigidev.attendance.service;

import com.avikdigidev.attendance.constants.AttendanceConstants;
import com.avikdigidev.attendance.dto.response.EmployeeStatusResponse;
import com.avikdigidev.attendance.model.AttendanceRecord;
import com.avikdigidev.attendance.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    // Method to calculate end of day total hours and update attendance status
    public EmployeeStatusResponse getAttendanceStatus(String employeeId) {
        EmployeeStatusResponse response = new EmployeeStatusResponse();
        response.setEmployeeId(employeeId);

        // Retrieve all swipe-in records for the employee for the current day
        List<AttendanceRecord> swipeInRecords = attendanceRepository.findFirstByEmployeeIdAndDateOrderByTimestampDesc(employeeId, LocalDateTime.now().toLocalDate());

        if (swipeInRecords.isEmpty()) {
            response.setStatus(AttendanceConstants.ABSENT); // Employee didn't swipe in, so mark as absent
        }

        // Assuming the last swipe-in is considered as the start of the workday
        LocalDateTime startOfDay = swipeInRecords.get(0).getSwipeInTimestamp().toLocalDate().atStartOfDay();

        // Retrieve the last swipe-out record for the employee for the current day (if any)
        AttendanceRecord lastSwipeOutRecord = attendanceRepository.findFirstByEmployeeIdAndDateAndSwipeOutTimestampNotNullOrderBySwipeOutTimestampDesc(employeeId, LocalDateTime.now().toLocalDate());

        // If no swipe-out record found, use current time as the end of the workday
        LocalDateTime endOfDay = lastSwipeOutRecord != null ? lastSwipeOutRecord.getSwipeOutTimestamp() : LocalDateTime.now();

        // Calculate total hours worked
        long totalHours =  Duration.between(startOfDay, endOfDay).toHours();
        response.setTotalHours((double) totalHours);
        // Determine attendance status based on total hours
        response.setStatus(calculateAttendanceStatus(totalHours));

        return response;
    }

    // Method to calculate attendance status based on total hours worked
    private String calculateAttendanceStatus(long totalHours) {
        if (totalHours < 4) {
            return AttendanceConstants.ABSENT;
        } else if (totalHours >= 4 && totalHours < 8) {
            return AttendanceConstants.HALF_DAY;
        } else {
            return AttendanceConstants.PRESENT;
        }
    }
}

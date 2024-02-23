package com.avikdigidev.attendance.service;

import com.avikdigidev.attendance.dto.response.AttendanceStatus;
import com.avikdigidev.attendance.dto.response.EmployeeStatusResponse;
import com.avikdigidev.attendance.model.AttendanceRecord;
import com.avikdigidev.attendance.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Value("${min.working.hours}")
    int minHours;
    @Value("${max.working.hours}")
    int maxHours;


    // Method to calculate end of day total hours and update attendance status
    public EmployeeStatusResponse getAttendanceStatus(String employeeId) {
        EmployeeStatusResponse response = new EmployeeStatusResponse();
        response.setEmployeeId(employeeId);

        // Retrieve all swipe-in records for the employee for the current day
        Optional<AttendanceRecord> swipeInRecords = attendanceRepository.getRecords(employeeId, LocalDateTime.now().toLocalDate());

        if (!swipeInRecords.isPresent()) {
            response.setStatus(AttendanceStatus.ABSENT); // Employee didn't swipe in, so mark as absent
        }

        // Assuming the last swipe-in is considered as the start of the workday
        LocalDateTime startOfDay = swipeInRecords.get().getSwipeInTimestamp().toLocalDate().atStartOfDay();

        // Retrieve the last swipe-out record for the employee for the current day (if any)
        AttendanceRecord lastSwipeOutRecord = attendanceRepository.findFirstByEmployeeIdAndDateAndSwipeOutTimestampNotNullOrderBySwipeOutTimestampDesc(employeeId, LocalDateTime.now().toLocalDate());

        // If no swipe-out record found, use current time as the end of the workday
        LocalDateTime endOfDay = lastSwipeOutRecord != null ? lastSwipeOutRecord.getSwipeOutTimestamp() : LocalDateTime.now();

        // Calculate total hours worked
        long totalHours = Duration.between(startOfDay, endOfDay).toHours();
        response.setTotalHours((double) totalHours);
        // Determine attendance status based on total hours
        response.setStatus(calculateAttendanceStatus(totalHours));

        return response;
    }

    // Method to calculate attendance status based on total hours worked
    private AttendanceStatus calculateAttendanceStatus(long totalHours) {
        if (totalHours < minHours) {
            return AttendanceStatus.ABSENT;
        } else if (totalHours >= minHours && totalHours < maxHours) {
            return AttendanceStatus.HALF_DAY;
        } else {
            return AttendanceStatus.PRESENT;
        }
    }
}

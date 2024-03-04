package com.attendance.dto;

import com.attendance.exception.ApiError;
import lombok.Data;

import java.util.List;

@Data
public class AttendanceRecord {

    private List<Attendance> attendanceList;
    private ApiError apiError;
}

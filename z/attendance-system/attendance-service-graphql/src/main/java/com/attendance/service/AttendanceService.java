package com.attendance.service;

import java.util.List;
import com.attendance.dto.AttendanceRecord;
import com.attendance.exception.ApiError;
import com.attendance.proxies.AttendanceSystemServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.attendance.dto.Attendance;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AttendanceService {

    @Autowired
    private AttendanceSystemServiceProxy attendanceSystemServiceProxy;

    public AttendanceRecord getAttendanceData(int employeeId) {
        AttendanceRecord attendanceRecord = new AttendanceRecord();
        try {

            List<Attendance> attendances = attendanceSystemServiceProxy.retrieveExchangeValue(employeeId);
            log.info(attendances.toString());
            attendanceRecord.setAttendanceList(attendances);
        } catch (Exception exception) {
            log.error("Error Occurred :: "+ exception.getMessage());
            attendanceRecord.setApiError(new ApiError(HttpStatus.BAD_REQUEST, exception.getMessage(), exception));
        }
        return attendanceRecord;
    }


}

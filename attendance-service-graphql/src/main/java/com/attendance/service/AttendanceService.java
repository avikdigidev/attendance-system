package com.attendance.service;

import com.attendance.dto.Attendance;
import com.attendance.proxies.AttendanceSystemServiceProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AttendanceService {

    @Autowired
    private AttendanceSystemServiceProxy attendanceSystemServiceProxy;


    public List<Attendance> getAttendanceData(int employeeId) {
        List<Attendance> attendances = attendanceSystemServiceProxy.retrieveExchangeValue(employeeId);
        log.info(attendances.toString());
        return attendances;
    }


}

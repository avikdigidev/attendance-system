package com.attendance.service;

import java.util.ArrayList;
import java.util.List;

import com.attendance.proxies.AttendanceSystemServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.attendance.dto.Attendance;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

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

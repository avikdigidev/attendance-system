package com.attendance.proxies;

import com.attendance.dto.Attendance;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="ATTENDANCE-SYSTEM-SERVICE")
public interface AttendanceSystemServiceProxy {

    @GetMapping("/api/attendance/getAttendance/{employeeId}")
    public List<Attendance> retrieveExchangeValue(
            @PathVariable int employeeId);


}

package com.attendance.calculation.system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeAttendance {

    private int employeeId;
    private String attendanceStatus;
    private Date date;
    private String totalTime;

}

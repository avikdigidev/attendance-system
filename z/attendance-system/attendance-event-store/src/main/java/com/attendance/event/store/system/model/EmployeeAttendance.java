package com.attendance.event.store.system.model;

import lombok.Data;

import java.sql.Date;

@Data
public class EmployeeAttendance {

    private int employeeId;
    private String attendanceStatus;
    private Date date;
    private String totalTime;
    private String emailId;

}

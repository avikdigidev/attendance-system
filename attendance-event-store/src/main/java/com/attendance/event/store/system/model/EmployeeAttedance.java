package com.attendance.event.store.system.model;

import lombok.Data;

import java.sql.Date;

@Data
public class EmployeeAttedance {

    private int employeeId;
    private String attendanceStatus;
    private Date date;
    private String totalTime;

}

package com.attendance.calculation.system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    private int employeeId;
    private String emailId;
    private String attendanceStatus;
    private Date date;
    private String totalTime;

}

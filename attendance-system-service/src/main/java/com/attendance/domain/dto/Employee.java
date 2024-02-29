package com.attendance.domain.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class Employee {
	
	private int employeeId;
	private String attendanceStatus;
	private Date date;
	private String totalTime;
}

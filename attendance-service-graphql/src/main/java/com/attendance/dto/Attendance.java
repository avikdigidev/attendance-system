package com.attendance.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class Attendance {

	private int employeeId;
	private String attendanceStatus;
	private Date date;
	private String totalTime;

}

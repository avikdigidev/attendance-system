package com.attendance.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class Attendance {
	
	private long employeeid;
	private String attendance;
	private Date date;
	private String totaltime;

}

package com.attendance.consumer.system.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

import java.sql.Date;
@IdClass(AttendanceId.class)
@Entity
@Data
public class Attendance {


	@Id
	private int employeeId;
	@Id
	private Date date;

	private String attendanceStatus;
	private String totalTime;
}

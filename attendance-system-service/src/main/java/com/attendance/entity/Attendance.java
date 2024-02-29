package com.attendance.entity;




import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Date;
import java.util.UUID;

@Entity
@Data
public class Attendance {
	

	@Id
	private String attendanceId = UUID.randomUUID().toString();
	private int employeeId;
	private String attendanceStatus;
	private Date date;
	private String totalTime;
}

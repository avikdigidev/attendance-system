package com.attendance.entity;




import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Date;
import java.util.UUID;

@Entity
@Data
public class Attendance {
	

	@Id
//	@GeneratedValue(generator = "uuid2")
//	@GenericGenerator(name = "uuid2", strategy = "uuid2")
//	@Column(name = "id", columnDefinition = "VARCHAR(255)")
	private String attendanceId = UUID.randomUUID().toString();
	private int employeeId;
	private String attendanceStatus;
	private Date date;
	private String totalTime;
	private String emailId;
}

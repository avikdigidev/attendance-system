package com.attendance.datafetcher;

import java.util.List;

import com.attendance.dto.AttendanceRecord;
import com.netflix.graphql.dgs.DgsEnableDataFetcherInstrumentation;
import org.springframework.beans.factory.annotation.Autowired;

import com.attendance.dto.Attendance;
import com.attendance.service.AttendanceService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

@DgsComponent
public class AttendanceDataFetcher {
	
	@Autowired
	private AttendanceService attendanceService;

	@DgsQuery(field =  "getAttendance")
	@DgsEnableDataFetcherInstrumentation(false)
	public AttendanceRecord getAttendance(@InputArgument int employeeId){
		return attendanceService.getAttendanceData(employeeId);
	}
}

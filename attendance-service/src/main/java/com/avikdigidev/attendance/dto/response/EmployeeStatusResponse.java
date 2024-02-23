package com.avikdigidev.attendance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeStatusResponse {
    private String employeeId;
    private Double totalHours;
    private AttendanceStatus status;
}

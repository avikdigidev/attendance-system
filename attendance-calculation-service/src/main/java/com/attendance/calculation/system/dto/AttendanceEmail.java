package com.attendance.calculation.system.dto;

import com.attendance.calculation.system.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceEmail {
    private Employee employee;
    private String subject;
    private String body;
}

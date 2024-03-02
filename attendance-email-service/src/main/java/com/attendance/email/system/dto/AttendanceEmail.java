package com.attendance.email.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceEmail {
    private Employee employee;
    private String subject;
    private String body;
}

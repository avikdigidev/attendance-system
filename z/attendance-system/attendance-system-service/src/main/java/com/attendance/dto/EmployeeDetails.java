package com.attendance.dto;

import com.attendance.exception.ApiError;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeDetails {

    private List<Employee> employee;
    private ApiError apiError;
}

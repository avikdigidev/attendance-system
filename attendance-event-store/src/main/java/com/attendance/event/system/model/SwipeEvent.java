package com.attendance.event.system.model;

import java.time.Instant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class SwipeEvent  {
    @NotNull
    @Min(value = 1, message = "Employee ID must be a positive integer")
    private Integer employeeId;
    @Email(message = "Email should be valid")
    private String emailId;

    private Instant eventTimestamp;

}

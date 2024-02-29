package com.attendance.event.system.model;

import java.time.Instant;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class SwipeEvent  {
    @Min(value = 1, message = "Employee ID must be a positive integer")
    private Integer employeeId;
    @NotNull(message = "Event timestamp must not be null")
    private Instant eventTimestamp;

}

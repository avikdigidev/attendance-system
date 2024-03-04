package com.attendance.event.store.system.model;

import java.time.Instant;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class SwipeEvent  {

    @NotNull
    @Min(value = 1, message = "Employee Id must be a positive integer")
    private int employeeId;
    private Instant eventTimestamp;

}

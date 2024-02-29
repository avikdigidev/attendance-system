package com.attendance.calculation.system.model;

import java.time.Instant;

import lombok.Data;
@Data
public class SwipeEvent  {
    private int employeeId;
    private Instant eventTimestamp;

}

package com.attendance.calculation.system.model;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SwipeEvent  {
    private int employeeId;
    private Instant eventTimestamp;

}

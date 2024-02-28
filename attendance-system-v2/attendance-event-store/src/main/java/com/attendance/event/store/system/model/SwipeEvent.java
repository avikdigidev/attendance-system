package com.attendance.event.store.system.model;

import java.time.Instant;

import lombok.Data;
@Data
public class SwipeEvent  {
    private int employeeId;
    private Instant eventTimestamp;

}

package com.avikdigidev.attendance.dto.request;

import lombok.*;

import java.time.*;

@Data
public class SwipeRequest {


    private String employeeId;
    private LocalDateTime eventTimestamp ;
    private EventType eventType;

}

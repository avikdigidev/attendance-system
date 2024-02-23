package com.avikdigidev.eventservice.model;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class SwipeEvent {
    private String employeeId;
    private LocalDateTime eventTimestamp ;
    private EventType eventType;

}

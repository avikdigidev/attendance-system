package com.attendance.event.store.system.dto;

import lombok.Data;

import java.time.Instant;
@Data
public class EventDetail extends Event {
    private Instant timestamp;
}
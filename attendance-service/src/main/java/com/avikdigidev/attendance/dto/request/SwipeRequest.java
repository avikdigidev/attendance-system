package com.avikdigidev.attendance.dto.request;

import lombok.*;

import java.time.*;

@Getter
@Setter
public class SwipeRequest {


    private String employeeId;
    private LocalDateTime swipeInTimestamp;
    private LocalDateTime swipeOutTimestamp;

}

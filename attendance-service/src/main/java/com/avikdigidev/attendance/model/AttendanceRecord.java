package com.avikdigidev.attendance.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class AttendanceRecord {

    @Id
    private String id;

    @Column
    private LocalDate date;
    @Column(nullable = false, name = "employee_id")
    private String employeeId;

    @Column(nullable = false, name = "swipe_in_timestamp")
    private LocalDateTime swipeInTimestamp;
    @Column(nullable = false, name = "swipe_out_timestamp")
    private LocalDateTime swipeOutTimestamp;

}


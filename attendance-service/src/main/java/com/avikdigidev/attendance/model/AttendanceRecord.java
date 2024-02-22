package com.avikdigidev.attendance.model;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate date;
    @Column(nullable = false, name = "employee_id")
    private String employeeId;

    @Column(nullable = false, name = "swipe_in_timestamp")
    private LocalDateTime swipeInTimestamp;
    @Column(nullable = false, name = "swipe_out_timestamp")
    private LocalDateTime swipeOutTimestamp;

}


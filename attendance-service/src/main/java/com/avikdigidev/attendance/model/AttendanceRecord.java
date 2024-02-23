package com.avikdigidev.attendance.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class AttendanceRecord {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    private String id;

    @Column(name = "date")
    private LocalDate date;
    @Id
    @Column(name = "employee_id")
    private String employeeId;

    @Column(nullable = false, name = "swipe_in_timestamp")
    private LocalDateTime swipeInTimestamp;
    @Column(nullable = false, name = "swipe_out_timestamp")
    private LocalDateTime swipeOutTimestamp;

}


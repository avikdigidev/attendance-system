package com.attendance.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class AttendanceId implements Serializable {

    private int employeeId;
    private Date date;

}

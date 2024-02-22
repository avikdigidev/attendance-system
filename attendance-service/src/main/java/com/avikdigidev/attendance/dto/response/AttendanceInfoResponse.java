package com.avikdigidev.attendance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceInfoResponse {
    private List<String> absentees;
    private List<String> halfDayWorkers;
    private List<String> fullDayWorkers;

}

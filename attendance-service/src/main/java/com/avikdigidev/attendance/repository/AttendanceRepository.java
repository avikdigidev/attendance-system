package com.avikdigidev.attendance.repository;


import com.avikdigidev.attendance.model.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceRecord, String> {


    List<AttendanceRecord> findFirstByEmployeeIdAndDateOrderBySwipeInTimestampDesc(String employeeId, LocalDate date);
    AttendanceRecord findFirstByEmployeeIdAndDateAndSwipeOutTimestampNotNullOrderBySwipeOutTimestampDesc(String employeeId, LocalDate date);

}


package com.avikdigidev.attendance.repository;


import com.avikdigidev.attendance.model.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceRecord, String> {

    @Query(value = "SELECT * FROM attendance_db.attendance_record WHERE employee_id = :employeeId AND date = :date ORDER BY swipe_in_timestamp DESC LIMIT 1", nativeQuery = true)
    Optional<AttendanceRecord> getRecords(@Param("employeeId") String employeeId, @Param("date") LocalDate date);

//    Optional<AttendanceRecord> findFirstByEmployeeIdAndDateOrderBySwipeInTimestampDesc(String employeeId, LocalDate date);
    AttendanceRecord findFirstByEmployeeIdAndDateAndSwipeOutTimestampNotNullOrderBySwipeOutTimestampDesc(String employeeId, LocalDate date);

}


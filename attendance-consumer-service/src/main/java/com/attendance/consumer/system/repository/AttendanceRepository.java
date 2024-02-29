package com.attendance.consumer.system.repository;

import com.attendance.consumer.system.entity.Attendance;
import com.attendance.consumer.system.entity.AttendanceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceId> {

}

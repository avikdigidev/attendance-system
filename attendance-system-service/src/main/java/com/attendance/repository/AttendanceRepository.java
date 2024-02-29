package com.attendance.repository;

import com.attendance.entity.Attendance;
import com.attendance.entity.AttendanceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceId> {

	List<Attendance> findAllByEmployeeId(int employeeId);

}

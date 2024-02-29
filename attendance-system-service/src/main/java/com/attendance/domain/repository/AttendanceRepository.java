package com.attendance.domain.repository;

import com.attendance.domain.entity.Attendance;
import com.attendance.domain.entity.AttendanceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceId> {

	List<Attendance> findAllByEmployeeId(int employeeId);

}

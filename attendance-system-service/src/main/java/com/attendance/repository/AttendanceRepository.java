package com.attendance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.attendance.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, String> {

	List<Attendance> findAllByEmployeeId(int employeeId);

}

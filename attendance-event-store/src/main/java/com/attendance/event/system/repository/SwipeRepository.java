package com.attendance.event.system.repository;


import com.attendance.event.system.dto.Event;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SwipeRepository extends CassandraRepository<Event, Integer> {

	@Query("SELECT employeeid, MAX(eventtimestamp) AS timestamp, eventtype " + "FROM event_details "
			+ "WHERE employeeid= :employeeId ORDER BY eventtimestamp DESC")
	Optional<Event> findByEmployeeIdAndDate(int employeeId);
}

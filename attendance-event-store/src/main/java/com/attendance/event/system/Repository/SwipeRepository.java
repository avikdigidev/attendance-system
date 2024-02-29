package com.attendance.event.system.Repository;


import com.attendance.event.system.dto.Event;
import com.attendance.event.system.dto.EventDetail;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


@Repository
public interface SwipeRepository extends CassandraRepository<Event, Integer> {

	@Query("SELECT employeeid, MAX(eventtimestamp) AS timestamp, eventtype " + "FROM event_details "
			+ "WHERE employeeid= :employeeId ORDER BY eventtimestamp DESC")
	Optional<Event> findByEmployeeIdAndDate(int employeeId);
}

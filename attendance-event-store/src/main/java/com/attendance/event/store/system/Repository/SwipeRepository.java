package com.attendance.event.store.system.Repository;

import com.attendance.event.store.system.dto.Event;
import com.attendance.event.store.system.dto.EventDetail;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface SwipeRepository extends CassandraRepository<Event, Integer> {
	@Query("SELECT employeeid, MIN(eventtimestamp) AS timestamp " + "FROM event_details " + "WHERE eventtype = 'SWIPE_IN' "
			+ "AND eventtimestamp >= :startDate " + "AND eventtimestamp < :endDate "
			+ "GROUP BY employeeid ALLOW FILTERING")
	List<EventDetail> findFirstSwipeInByEmployeeAndDate(@Param("startDate") Instant startDate,
			@Param("endDate") Instant endDate);

	@Query("SELECT employeeid, MAX(eventtimestamp) AS timestamp " + "FROM event_details " + "WHERE eventtype = 'SWIPE_OUT' "
			+ "AND eventtimestamp >= :startDate " + "AND eventtimestamp < :endDate "
			+ "GROUP BY employeeid ALLOW FILTERING")
	List<EventDetail> findLastSwipeOutByEmployeeAndDate(@Param("startDate") Instant startDate,
			@Param("endDate") Instant endDate);

	@Query("SELECT employeeid, MAX(eventtimestamp) AS timestamp, eventtype " + "FROM event_details "
			+ "WHERE employeeid= :employeeId ORDER BY eventtimestamp DESC")
	Optional<Event> findByEmployeeIdAndDate(int employeeId);
}

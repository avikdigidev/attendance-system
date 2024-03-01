package com.attendance.calculation.system.repository;

import com.attendance.calculation.system.dto.EventDetail;
import com.attendance.calculation.system.dto.Event;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface EventRepository extends CassandraRepository<Event, Integer> {
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


}

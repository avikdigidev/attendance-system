package com.attendance.event.system.dto;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
	
import java.time.Instant;
@Table("event_details")
@Data
public class Event {

    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private Integer employeeid;

    @PrimaryKeyColumn(ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private Instant eventtimestamp;

    @Column
    private String eventtype;

    @Column
    private String emailid;

}
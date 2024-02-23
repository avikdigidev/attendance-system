package com.avikdigidev.eventservice.model;

import lombok.*;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;

import static com.avikdigidev.eventservice.constants.AppConstants.EVENT_DETAILS;

@Table(EVENT_DETAILS)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Event {


    @PrimaryKeyColumn(name = "employeeid", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String employeeId;
    @PrimaryKeyColumn(name = "eventtimestamp", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private Instant eventTimestamp;
    @Column(value = "eventtype")
    private String eventType;


}
package com.attendance.calculation.system.services;

import com.attendance.calculation.system.dto.EventDetail;
import com.attendance.calculation.system.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class AttendanceCalculatorServiceTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private EventRepository eventRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private AttendanceCalculatorService attendanceCalculatorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalculateTotalHoursBySwipeInAndOutDate() {
        // Mocking data
        List<EventDetail> firstSwipeInByEmployeeAndDate = Arrays.asList(
                createEventDetail(1, Instant.now(), "SwipeIn", Instant.now()),
                createEventDetail(2, Instant.now(), "SwipeIn", Instant.now()),
                createEventDetail(3, Instant.now(), "SwipeIn", Instant.now()) ,
                createEventDetail(4, Instant.now(), "SwipeIn", null) ,
                createEventDetail(5, Instant.now(), "SwipeIn", Instant.now())
        );
        List<EventDetail> lastSwipeOutByEmployeeAndDate  = Arrays.asList(
                createEventDetail(1, Instant.now(), "SwipeOut", Instant.now().plus(Duration.ofHours(1))),
                createEventDetail(2, Instant.now(), "SwipeOut", Instant.now().plus(Duration.ofHours(5))),
                createEventDetail(3, Instant.now(), "SwipeOut", Instant.now().plus(Duration.ofHours(9))),
                createEventDetail(4, Instant.now(), "SwipeOut", Instant.now().plus(Duration.ofHours(9))),
                createEventDetail(5, Instant.now(), "SwipeOut", null)
        );
        when(eventRepository.findFirstSwipeInByEmployeeAndDate(any(), any())).thenReturn(firstSwipeInByEmployeeAndDate);
        when(eventRepository.findLastSwipeOutByEmployeeAndDate(any(), any())).thenReturn(lastSwipeOutByEmployeeAndDate);

        int totalHours = attendanceCalculatorService.calculateTotalHoursBySwipeInAndOutDate();


        Assertions.assertEquals(5, totalHours);
    }




    private EventDetail createEventDetail(int employeeId, Instant eventTimestamp, String eventType, Instant timestamp) {
        EventDetail eventDetail = new EventDetail();
        eventDetail.setEmployeeid(employeeId);
        eventDetail.setEventtimestamp(eventTimestamp);
        eventDetail.setEventtype(eventType);
        eventDetail.setTimestamp(timestamp);
        return eventDetail;
    }
}

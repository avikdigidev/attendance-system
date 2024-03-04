package com.attendance.event.store.system.services;

import com.attendance.event.store.system.Repository.SwipeRepository;
import com.attendance.event.store.system.Services.AttendanceCalculatorService;
import com.attendance.event.store.system.constants.AppConstants;
import com.attendance.event.store.system.dto.EventDetail;
import com.attendance.event.store.system.exception.EntityNotFoundException;
import com.attendance.event.store.system.model.EmployeeAttendance;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class AttendanceCalculatorServiceTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private SwipeRepository eventRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private AttendanceCalculatorService attendanceCalculatorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("test calculate attendance - success")
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
        when(eventRepository.findFirstSwipeInByEmployeeAndDate(any(), any()))
                .thenReturn(firstSwipeInByEmployeeAndDate);

        when(eventRepository.findLastSwipeOutByEmployeeAndDate(any(), any()))
                .thenReturn(lastSwipeOutByEmployeeAndDate);

        int totalHours = attendanceCalculatorService.calculateTotalHoursBySwipeInAndOutDate();

        Assertions.assertEquals(0, totalHours);
    }

    @Test
    public void giveEmptyListInSwipeInFetch_WhenCalculateTotalHoursBySwipeInAndOutDate_ThenReturnException() {

        when(eventRepository.findFirstSwipeInByEmployeeAndDate(any(), any()))
                .thenReturn(Collections.emptyList());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> attendanceCalculatorService.calculateTotalHoursBySwipeInAndOutDate());

        EmployeeAttendance employeeAttendance = new EmployeeAttendance();
        employeeAttendance.setAttendanceStatus(AppConstants.ABSENT);
        employeeAttendance.setDate(new Date(System.currentTimeMillis()));
        employeeAttendance.setTotalTime("12:00");
        employeeAttendance.setEmployeeId(1);

        Assertions.assertAll(

                () -> Assertions.assertNotNull(employeeAttendance.getAttendanceStatus()),
                () -> Assertions.assertEquals(1, employeeAttendance.getEmployeeId()),
                () -> Assertions.assertNotNull(employeeAttendance.getTotalTime()),
                () -> Assertions.assertNotNull(employeeAttendance.getDate())
        );
    }

    @Test
    public void giveEmptyListInSwipeOutFetch_WhenCalculateTotalHoursBySwipeInAndOutDate_ThenReturnException2() {

        EventDetail eventDetail = createEventDetail(1, Instant.now(), "SwipeIn", Instant.now());

        when(eventRepository.findFirstSwipeInByEmployeeAndDate(any(), any()))
                .thenReturn(Collections.singletonList(eventDetail));

        when(eventRepository.findLastSwipeOutByEmployeeAndDate(any(), any()))
                .thenReturn(Collections.emptyList());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> attendanceCalculatorService.calculateTotalHoursBySwipeInAndOutDate());
    }

    private EventDetail createEventDetail(int employeeId, Instant eventTimestamp,
                                          String eventType, Instant timestamp) {
        EventDetail eventDetails = new EventDetail();
        eventDetails.setEmployeeid(employeeId);
        eventDetails.setEventtimestamp(eventTimestamp);
        eventDetails.setEventtype(eventType);
        eventDetails.setTimestamp(timestamp);
        return eventDetails;
    }
}

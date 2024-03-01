package com.attendance.event.system.services;
import com.attendance.event.system.dto.Event;
import com.attendance.event.system.dto.EventType;
import com.attendance.event.system.model.SwipeEvent;
import com.attendance.event.system.repository.SwipeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SwipeServiceTest {

    @Mock
    private SwipeRepository swipeRepository;

    @InjectMocks
    private SwipeService swipeService;


    @Test
    public void testProcessSwipeEvent_ExistingEvent() {
        // Mocking existing event
        int employeeId = 1;
        Event existingEvent = new Event();
        existingEvent.setEmployeeid(employeeId);
        existingEvent.setEventtype(EventType.SWIPE_IN.name());
        when(swipeRepository.findByEmployeeIdAndDate(employeeId)).thenReturn(Optional.of(existingEvent));

        // Mocking swipe event
        SwipeEvent swipeEvent = new SwipeEvent();
        swipeEvent.setEmployeeId(employeeId);
        swipeEvent.setEventTimestamp(Instant.now());

        // Test processing swipe event
        String result = swipeService.processSwipeEvent(swipeEvent);

        // Verify behavior
        assertEquals("You have SWIPE_OUT successfully at " + swipeEvent.getEventTimestamp(), result);
        verify(swipeRepository).save(existingEvent);
    }

    @Test
    public void testProcessSwipeEvent_NoExistingEvent() {
        // Mocking no existing event
        int employeeId = 1;
        when(swipeRepository.findByEmployeeIdAndDate(employeeId)).thenReturn(Optional.empty());

        // Mocking swipe event
        SwipeEvent swipeEvent = new SwipeEvent();
        swipeEvent.setEmployeeId(employeeId);
        swipeEvent.setEventTimestamp(Instant.now());

        // Test processing swipe event
        String result = swipeService.processSwipeEvent(swipeEvent);

        // Verify behavior
        assertEquals("You have SWIPE_IN successfully at " + swipeEvent.getEventTimestamp(), result);
        verify(swipeRepository).save(any(Event.class));
    }

    @Test
    public void testProcessSwipeEvent_NullTimestamp() {
        // Mocking existing event
        int employeeId = 1;
        Event existingEvent = new Event();
        existingEvent.setEmployeeid(employeeId);
        existingEvent.setEventtype(EventType.SWIPE_IN.name());
        when(swipeRepository.findByEmployeeIdAndDate(employeeId)).thenReturn(Optional.of(existingEvent));

        // Mocking swipe event with null timestamp
        SwipeEvent swipeEvent = new SwipeEvent();
        swipeEvent.setEmployeeId(employeeId);

        // Test processing swipe event
        String result = swipeService.processSwipeEvent(swipeEvent);

        // Verify behavior
        assertEquals("You have SWIPE_OUT successfully at " + existingEvent.getEventtimestamp(), result);
        verify(swipeRepository).save(existingEvent);
    }
}

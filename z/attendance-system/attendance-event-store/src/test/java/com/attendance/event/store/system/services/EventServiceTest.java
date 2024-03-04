package com.attendance.event.store.system.services;
import com.attendance.event.store.system.Repository.SwipeRepository;
import com.attendance.event.store.system.Services.EventService;
import com.attendance.event.store.system.dto.Event;
import com.attendance.event.store.system.dto.EventType;
import com.attendance.event.store.system.model.SwipeEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EventServiceTest {

    @Mock
    private SwipeRepository swipeRepository;

    @InjectMocks
    private EventService swipeService;


    @Test
    public void testProcessSwipeEvent_ExistingEvent() {
        // Mocking existing event
        int employeeId = 1;
        Event existingEvent = getEvent(employeeId);
        when(swipeRepository.findByEmployeeIdAndDate(employeeId)).thenReturn(Optional.of(existingEvent));

        // Mocking swipe event
        SwipeEvent swipeEvent = new SwipeEvent();
        swipeEvent.setEmployeeId(employeeId);
        swipeEvent.setEventTimestamp(Instant.now());

        // Test processing swipe event
        String result = swipeService.processSwipeEvent(swipeEvent);

        // Verify behavior
        assertTrue(result.startsWith("You have SWIPE_OUT successfully"));
        verify(swipeRepository).save(existingEvent);
    }

    @Test
    public void testProcessSwipeEvent_ExceptionTest() {
        when(swipeRepository.findByEmployeeIdAndDate(anyInt())).thenThrow(new RuntimeException());
        // Test processing swipe event
        String result = swipeService.processSwipeEvent(new SwipeEvent());

        assertThrows(Exception.class, () ->swipeRepository.findByEmployeeIdAndDate(1));
    }

    private static Event getEvent(int employeeId) {
        Event existingEvent = new Event();
        existingEvent.setEmployeeid(employeeId);
        existingEvent.setEventtype(EventType.SWIPE_IN.name());
        return existingEvent;
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
        assertNotNull(result);
        verify(swipeRepository).save(any(Event.class));
    }

    @Test
    public void testProcessSwipeEvent_NullTimestamp() {
        // Mocking existing event
        int employeeId = 1;
        Event existingEvent = getEvent(employeeId);
        when(swipeRepository.findByEmployeeIdAndDate(employeeId)).thenReturn(Optional.of(existingEvent));

        // Mocking swipe event with null timestamp
        SwipeEvent swipeEvent = new SwipeEvent();
        swipeEvent.setEmployeeId(employeeId);

        // Test processing swipe event
        String result = swipeService.processSwipeEvent(swipeEvent);

        // Verify behavior
        assertAll(
                () -> assertTrue(result.startsWith("You have SWIPE_OUT successfully")),
                () -> assertNotNull(result));
        verify(swipeRepository).save(existingEvent);
    }
}

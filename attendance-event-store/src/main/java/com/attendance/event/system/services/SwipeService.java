package com.attendance.event.system.services;


import com.attendance.event.system.repository.SwipeRepository;
import com.attendance.event.system.dto.Event;
import com.attendance.event.system.dto.EventType;
import com.attendance.event.system.model.SwipeEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class SwipeService {

    private static final String EMAIL_DOMAIN = "@gmail.com";
    @Autowired
    private SwipeRepository swipeRepository;

    public String processSwipeEvent(SwipeEvent swipeEvent) {
        Optional<Event> eventOptional = swipeRepository.findByEmployeeIdAndDate(swipeEvent.getEmployeeId());
        Event newEvent = eventOptional.orElseGet(Event::new);
        if (eventOptional.isPresent() && eventOptional.get().getEventtype() != null) {
            String eventType = eventOptional.get().getEventtype();
            newEvent.setEventtype(eventType.equals(EventType.SWIPE_IN.name()) ?
                    EventType.SWIPE_OUT.name() : EventType.SWIPE_IN.name());
        } else {
            newEvent.setEventtype(EventType.SWIPE_IN.name());
        }
        if (StringUtils.isNotBlank(swipeEvent.getEmailId())) {
            newEvent.setEmailid(swipeEvent.getEmailId());
        } else {
            newEvent.setEmailid(swipeEvent.getEmployeeId() + EMAIL_DOMAIN);
        }
        if (swipeEvent.getEventTimestamp() != null) {
            newEvent.setEventtimestamp(swipeEvent.getEventTimestamp());
        } else {
            newEvent.setEventtimestamp(Instant.now());
        }
        newEvent.setEmployeeid(swipeEvent.getEmployeeId());
        swipeRepository.save(newEvent);
        return "You have " + newEvent.getEventtype() + " successfully at " + newEvent.getEventtimestamp();
    }
}

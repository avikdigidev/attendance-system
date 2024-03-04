package com.attendance.event.store.system.Services;

import com.attendance.event.store.system.Repository.SwipeRepository;
import com.attendance.event.store.system.constants.AppConstants;
import com.attendance.event.store.system.dto.Event;
import com.attendance.event.store.system.dto.EventType;
import com.attendance.event.store.system.model.SwipeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class EventService {

	@Autowired
	private SwipeRepository swipeRepository;

	/*
	* this method process incoming swipe event
	* first it checks what's the last status of employee, is it swipe in or out.
	* based on that it will update the event type.
	* and store swipe events to db.
	* return string message upon successfully completion.
	* */
	public String processSwipeEvent(SwipeEvent swipeEvent) {
		try {
			Optional<Event> event = swipeRepository.findByEmployeeIdAndDate(swipeEvent.getEmployeeId());
			Event newEvent = event.orElseGet(Event::new);;
			log.info("event :: " + event);
			if (event.isPresent() && event.get().getEventtype() != null) {
				String eventType = event.get().getEventtype();
				if (eventType.equals(EventType.SWIPE_IN.name()))
					newEvent.setEventtype(EventType.SWIPE_OUT.name());
				else
					newEvent.setEventtype(EventType.SWIPE_IN.name());
			} else
				newEvent.setEventtype(EventType.SWIPE_IN.name());

			newEvent.setEventtimestamp(Instant.now());
			newEvent.setEmployeeid(swipeEvent.getEmployeeId());
			newEvent.setEmailid(swipeEvent.getEmployeeId() + AppConstants.EMAIL_DOMAIN);
			swipeRepository.save(newEvent);
			return "You have " + newEvent.getEventtype() + " successfully at " + newEvent.getEventtimestamp();
		} catch (Exception exception){
			log.error("Error occurred :: "+exception.getMessage());
			return "Error occurred during process";
		}
	}
}

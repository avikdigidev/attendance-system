package com.attendance.event.store.system.Services;

import com.attendance.event.store.system.Repository.SwipeRepository;
import com.attendance.event.store.system.dto.Event;
import com.attendance.event.store.system.dto.EventType;
import com.attendance.event.store.system.model.SwipeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class SwipeService {

	@Autowired
	private SwipeRepository swipeRepository;

	public String processSwipeEvent(SwipeEvent swipeEvent) {
		Optional<Event> event = swipeRepository.findByEmployeeIdAndDate(swipeEvent.getEmployeeId());
		Event newEvent = new Event();
		log.info("event :: " + event);
		if (event.isPresent() && event.get().getEventtype()!=null) {
			String eventType = event.get().getEventtype();
			if (eventType.equals(EventType.SWIPE_IN.name()))
				newEvent.setEventtype(EventType.SWIPE_OUT.name());
			else
				newEvent.setEventtype(EventType.SWIPE_IN.name());
		} else
			newEvent.setEventtype(EventType.SWIPE_IN.name());

		newEvent.setEventtimestamp(swipeEvent.getEventTimestamp());
		newEvent.setEmployeeid(swipeEvent.getEmployeeId());
		swipeRepository.save(newEvent);
		return "You have " + newEvent.getEventtype() + " successfully at " + newEvent.getEventtimestamp();
	}
}

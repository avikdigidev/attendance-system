package com.attendance.event.store.system.Controller;

import com.attendance.event.store.system.Services.AttendanceCalculatorService;
import com.attendance.event.store.system.Services.EventService;
import com.attendance.event.store.system.model.SwipeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class EventController {

    @Autowired
    private EventService eventService;
    
    @Autowired
	private AttendanceCalculatorService attendanceService;

    /*
    * this method just simply call service method to store swipe in/out data
    * */
    @PostMapping("/swipe")
    public ResponseEntity<String> swipeEvent(@Valid @RequestBody SwipeEvent swipeEvent){
       String message  = eventService.processSwipeEvent(swipeEvent);
       return ResponseEntity.ok(message);
   }

   /*
   * this method calculates the attendance of each employee
   * */
	@GetMapping("/calculateAttendance")
	private ResponseEntity<String> calculateEmployeeAttendance() {
		int records = attendanceService.calculateTotalHoursBySwipeInAndOutDate();
		return ResponseEntity.ok(records + " records updated");
	}
}

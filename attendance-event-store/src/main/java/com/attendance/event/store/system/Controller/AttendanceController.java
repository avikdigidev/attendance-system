package com.attendance.event.store.system.Controller;

import com.attendance.event.store.system.Services.AttendanceCalculatorService;
import com.attendance.event.store.system.Services.SwipeService;
import com.attendance.event.store.system.model.SwipeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class AttendanceController {

    @Autowired
    private SwipeService swipeService;
    
    @Autowired
	private AttendanceCalculatorService attendanceService;


    @PostMapping("/swipe")
    public ResponseEntity<String> swipeEvent(@RequestBody SwipeEvent swipeEvent){
       String message  = swipeService.processSwipeEvent(swipeEvent);
       return ResponseEntity.ok(message);
   }
    
	@GetMapping("/calculateAttendance")
	private ResponseEntity<String> calculateEmployeeAttendance() {
		int records = attendanceService.calculateTotalHoursBySwipeInAndOutDate();
		return ResponseEntity.ok(records + " records updated");
	}
}

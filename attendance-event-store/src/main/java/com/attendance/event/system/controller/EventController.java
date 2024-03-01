package com.attendance.event.system.controller;


import com.attendance.event.system.services.SwipeService;
import com.attendance.event.system.model.SwipeEvent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class EventController {

    @Autowired
    private SwipeService swipeService;

    @PostMapping("/swipe")
    public ResponseEntity<String> swipeEvent(@Valid @RequestBody SwipeEvent swipeEvent){
       String message  = swipeService.processSwipeEvent(swipeEvent);
       return ResponseEntity.ok(message);
   }


}

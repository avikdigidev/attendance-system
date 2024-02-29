package com.attendance.domain.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AttendanceControllerAdvice extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> exceptionHandler(RuntimeException e){
		
		return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		
	}
}

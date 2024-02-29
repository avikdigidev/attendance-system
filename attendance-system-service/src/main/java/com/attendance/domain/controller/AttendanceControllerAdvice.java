package com.attendance.domain.controller;

import com.attendance.domain.exception.NoDataFoundException;
import com.attendance.domain.exception.model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AttendanceControllerAdvice {



	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ExceptionHandler(NoDataFoundException.class)
	public ResponseEntity<ErrorDetails> handleNoDataFoundException(NoDataFoundException ex) {
		ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, "NF_001", Arrays.asList(ex.getMessage()));
		return ResponseEntity.badRequest().body(errorDetails);
	}
}

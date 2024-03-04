package com.attendance.controller;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AttendanceControllerAdviceTest {

    @Test
    void testExceptionHandler() {

        AttendanceControllerAdvice advice = new AttendanceControllerAdvice();
        RuntimeException exception = new RuntimeException("Test exception message");

        ResponseEntity<?> responseEntity = advice.exceptionHandler(exception);

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> Assertions.assertEquals("Test exception message", responseEntity.getBody()));
    }
}


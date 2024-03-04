package com.attendance.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /*
     * Handles EntityNotFoundException. Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     *
     * @param ex the EntityNotFoundException
     * @return the ApiError object
     */
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<Object> handleEntityNotFound(CustomException ex) {
        ApiError apiError = new ApiError(NOT_FOUND);
        apiError.setMessage(null != ex.getMessage() ? ex.getMessage(): "Error Occurred Please check debug message for more details");
        apiError.setDebugMessage(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
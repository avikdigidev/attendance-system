package com.attendance.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RestExceptionHandlerTest {

    @Test
    void testHandleEntityNotFound() {

        CustomException mockException = new CustomException("Error");

        RestExceptionHandler handler = new RestExceptionHandler();

        ResponseEntity<Object> responseEntity = handler.handleEntityNotFound(mockException);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        ApiError apiError = (ApiError) responseEntity.getBody();
        assert apiError != null;
        assert apiError.getTimestamp() != null;
        assert apiError.getDebugMessage() != null;
        Assertions.assertAll(
                () -> Assertions.assertNotNull(apiError.getMessage()),
                () -> Assertions.assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus()));
    }

}
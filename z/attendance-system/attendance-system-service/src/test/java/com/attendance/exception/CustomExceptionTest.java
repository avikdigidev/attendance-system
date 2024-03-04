package com.attendance.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class CustomExceptionTest {

    @Test
    void testEntityNotFoundExceptionConstructor1() {
        String message = "Error Occurred";
        CustomException exception = new CustomException(message);

        Assertions.assertNull(exception.getMessage());
    }
}

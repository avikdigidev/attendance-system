package com.attendance.event.store.system.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class EntityNotFoundExceptionTest {

    @Test
    void testEntityNotFoundExceptionConstructor1() {
        String message = "Error Occurred";
        EntityNotFoundException exception = new EntityNotFoundException(message);

        Assertions.assertNull(exception.getMessage());
    }

    @Test
    void testEntityNotFoundExceptionConstructor2() {
        Class<?> clazz = EntityNotFoundException.class;
        String[] searchParams = {"employee", "1", "eventTimestamp", "value2"};

        EntityNotFoundException exception = new EntityNotFoundException(clazz, searchParams);

        Assertions.assertEquals("EntityNotFoundException was not found for parameters {employee=1, eventTimestamp=value2}", exception.getMessage());
    }

}

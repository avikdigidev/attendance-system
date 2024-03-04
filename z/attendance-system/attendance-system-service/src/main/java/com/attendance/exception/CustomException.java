package com.attendance.exception;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class CustomException extends RuntimeException {

    public CustomException(String message) {
        super();
    }
}
package com.attendance.domain.exception.model;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@Data
public class ErrorDetails {
    private HttpStatus status;
    private String code;
    private List<String> errors;

    public ErrorDetails(HttpStatus status, String code, List<String> errors) {
        super();
        this.status = status;
        this.code = code;
        this.errors = errors;
    }

    public ErrorDetails(HttpStatus status, String code, String error) {
        super();
        this.status = status;
        this.code = code;
        errors = Arrays.asList(error);
    }
}

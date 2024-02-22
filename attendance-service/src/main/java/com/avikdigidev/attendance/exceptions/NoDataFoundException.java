package com.avikdigidev.attendance.exceptions;


public class NoDataFoundException extends RuntimeException {
    private final String errorCode;


    public NoDataFoundException(String errorMessage, String errorCode) {
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

package com.findToilet.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final HttpStatus status;

    public CustomException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        int statusCode = exceptionCode.getStatusCode();
        this.status = HttpStatus.valueOf(statusCode);
    }
}

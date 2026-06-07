package com.hintro.meeting_intelligence.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException  extends RuntimeException{
    private final String errorCode;
    private final HttpStatus httpStatus;

    public AppException(String errorCode, String message, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    // Convenience factories
    public static AppException notFound(String message) {
        return new AppException("NOT_FOUND", message, HttpStatus.NOT_FOUND);
    }

    public static AppException badRequest(String message) {
        return new AppException("BAD_REQUEST", message, HttpStatus.BAD_REQUEST);
    }

    public static AppException unauthorized(String message) {
        return new AppException("UNAUTHORIZED", message, HttpStatus.UNAUTHORIZED);
    }

    public static AppException conflict(String message) {
        return new AppException("CONFLICT", message, HttpStatus.CONFLICT);
    }
}

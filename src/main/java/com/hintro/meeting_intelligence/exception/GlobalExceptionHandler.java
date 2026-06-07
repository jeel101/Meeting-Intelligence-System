package com.hintro.meeting_intelligence.exception;

import org.springframework.validation.FieldError;
import java.util.stream.Collectors;
import com.hintro.meeting_intelligence.dto.response.ErrorResponseDto;
import com.hintro.meeting_intelligence.logging.TraceFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private String getTraceId() {
        String traceId = MDC.get(TraceFilter.TRACE_ID_MDC_KEY);
        return traceId != null ? traceId : "unknown";
    }

    // Custom app exceptions
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponseDto<Void>> handleAppException(AppException ex, HttpServletRequest request) {
        log.error("AppException [{}] {} {} - {}", getTraceId(),
                request.getMethod(), request.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus())
                .body(ErrorResponseDto.error(ex.getErrorCode(), ex.getMessage(), getTraceId()));
    }

    // Validation errors (@Valid failed)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("Validation failed [{}]: {}", getTraceId(), message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.error("VALIDATION_ERROR", message, getTraceId()));
    }

    // Generic RuntimeException (fallback)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto<Void>> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        log.error("Unhandled RuntimeException [{}] {} {} - {}",
                getTraceId(), request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDto.error("INTERNAL_ERROR", ex.getMessage(), getTraceId()));
    }

    // Catch-all
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto<Void>> handleException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error [{}] {} {} - {}",
                getTraceId(), request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDto.error("INTERNAL_ERROR", "An unexpected error occurred", getTraceId()));
    }
}

package com.ishwor.expenses.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        errorResponse.put("timestamp", Instant.now());
        errorResponse.put("status", HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}

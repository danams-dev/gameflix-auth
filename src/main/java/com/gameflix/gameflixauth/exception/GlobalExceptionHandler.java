package com.gameflix.gameflixauth.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

// handles API errors, including duplicate email registration errors
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEmail(
            DataIntegrityViolationException exception) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", "Email is already registered"));
    }
}
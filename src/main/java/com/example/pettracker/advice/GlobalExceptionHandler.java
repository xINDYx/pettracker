package com.example.pettracker.advice;

import com.example.pettracker.dto.response.ErrorResponse;
import com.example.pettracker.exception.InvalidTrackerTypeException;
import com.example.pettracker.exception.PetNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(PetNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePetNotFound(PetNotFoundException ex) {
        log.error("Pet not found: {}", ex.getMessage(), ex);
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, "Not Found");
    }

    @ExceptionHandler(InvalidTrackerTypeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTrackerType(InvalidTrackerTypeException ex) {
        log.error("Invalid tracker type: {}", ex.getMessage(), ex);
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, "Invalid Tracker Type");
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, HttpStatus status, String error) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(LocalDateTime.now(), status.value(), error, ex.getMessage()));
    }
}

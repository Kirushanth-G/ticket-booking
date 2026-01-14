package com.example.booking_server.controllers;

import com.example.booking_server.exceptions.EventAlreadyExistsException;
import com.example.booking_server.exceptions.EventNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationErrors(MethodArgumentNotValidException e){
        Map<String,String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(),error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleMalformedJson(HttpMessageNotReadableException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Malformed JSON request");
        error.put("message", "Invalid input format. Please check your data types (e.g., date formats).");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(EventAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEventAlreadyExists(EventAlreadyExistsException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Event Already Exists");
        error.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleGenericException(EventNotFoundException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Event Not Found");
        error.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid Request");
        error.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}

package com.example.authservice.controller.handler;


import com.example.authservice.exceptions.EmailSendException;
import com.example.authservice.exceptions.InvalidPhoneNumberException;
import com.example.authservice.exceptions.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailSendException.class)
    public ResponseEntity<Map<String, Object>> handleEmailSendFailing(EmailSendException ex) {
        log.error("email failed {}", ex.getMessage(), ex);
        return ResponseEntity.status(500).body(Map.of(
                "timestamp", System.currentTimeMillis(),
                "status", 500,
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(InvalidPhoneNumberException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidPhoneNumber(InvalidPhoneNumberException ex) {
        log.error("invalid phone {}", ex.getMessage(), ex);
        return ResponseEntity.status(404).body(Map.of(
                "timestamp", System.currentTimeMillis(),
                "status", 404,
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException ex) {
        log.error("user not found {}", ex.getMessage(), ex);
        return ResponseEntity.status(404).body(Map.of(
                "timestamp", System.currentTimeMillis(),
                "status", 404,
                "message", ex.getMessage()
        ));
    }

}


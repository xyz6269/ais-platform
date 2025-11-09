package com.example.projectservice.controller.handler;

import com.example.projectservice.exceptions.ParticipantNotFoundException;
import com.example.projectservice.exceptions.ProjectNotFoundException;
import com.example.projectservice.exceptions.TaskNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProjectNotFound(ProjectNotFoundException ex) {
        log.error("project not found {}", ex.getMessage() , ex);
        return ResponseEntity.status(404).body(Map.of(
                "timestamp", System.currentTimeMillis(),
                "status", 404,
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(ParticipantNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleParticipantNotFound(ParticipantNotFoundException ex) {
        log.error("participant not found {}", ex.getMessage(), ex);
        return ResponseEntity.status(404).body(Map.of(
                "timestamp", System.currentTimeMillis(),
                "status", 404,
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTaskNotFound(TaskNotFoundException ex) {
        log.error("task not found {}", ex.getMessage(), ex);
        return ResponseEntity.status(404).body(Map.of(
                "timestamp", System.currentTimeMillis(),
                "status", 404,
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
        log.error("task not found {}", ex.getMessage(), ex);
        return ResponseEntity.status(401).body(Map.of(
                "timestamp", System.currentTimeMillis(),
                "status", 404,
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("task not found {}", ex.getMessage(), ex);
        return ResponseEntity.status(500).body(Map.of(
                "timestamp", System.currentTimeMillis(),
                "status", 500
        ));
    }
}

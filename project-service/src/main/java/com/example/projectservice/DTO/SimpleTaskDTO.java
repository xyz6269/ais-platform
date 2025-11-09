package com.example.projectservice.DTO;

import com.example.projectservice.entity.Task;

import java.time.Instant;
import java.util.UUID;

public record SimpleTaskDTO(
        UUID id,
        UUID projectId,
        String title,
        String description,
        String status,
        String priority,
        Instant dueDate,
        Instant createdAt,
        Instant completedAt
) {
    public static SimpleTaskDTO toDTO(Task task) {
        return new SimpleTaskDTO(
                task.getId(),
                task.getTaskProject().getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().toString(),
                task.getPriority().toString(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getCompletedAt()
        );
    }
}

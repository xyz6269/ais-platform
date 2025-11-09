package com.example.projectservice.DTO;

import com.example.projectservice.entity.Task;

import java.time.Instant;
import java.util.UUID;

public record TaskDTO(
        UUID id,
        UUID projectId,
        String title,
        String description,
        String status,
        String priority,
        Instant dueDate,
        Instant createdAt,
        Instant completedAt,
        ParticipantDTO assignedToParticipant,
        ProjectDTO taskProject
) {
    public static TaskDTO toDTO(Task task) {
        return new TaskDTO(
                task.getId(),
                task.getTaskProject().getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().toString(),
                task.getPriority().toString(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getCompletedAt(),
                ParticipantDTO.toDTO(task.getAssignedToParticipant()),
                ProjectDTO.toDTO(task.getTaskProject())
        );
    }
}
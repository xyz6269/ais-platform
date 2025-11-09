package com.example.projectservice.DTO;

import java.time.Instant;
import java.util.UUID;

public record CreateTaskDTO(
        UUID projectId,
        String title,
        String description,
        String priority,
        Instant dueDate
) {}

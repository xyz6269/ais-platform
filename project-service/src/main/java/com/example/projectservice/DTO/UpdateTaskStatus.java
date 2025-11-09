package com.example.projectservice.DTO;

import java.util.UUID;

public record UpdateTaskStatus(
        UUID id,
        String status
) {
}

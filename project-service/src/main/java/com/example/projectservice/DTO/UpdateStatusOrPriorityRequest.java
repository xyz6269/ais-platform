package com.example.projectservice.DTO;

import java.util.UUID;

public record UpdateStatusOrPriorityRequest(
        UUID id,
        String enumValue
) {}

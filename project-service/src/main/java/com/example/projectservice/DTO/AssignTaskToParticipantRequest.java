package com.example.projectservice.DTO;

import java.util.UUID;

public record AssignTaskToParticipantRequest(
        String participantEmail,
        UUID taskId
) {
}

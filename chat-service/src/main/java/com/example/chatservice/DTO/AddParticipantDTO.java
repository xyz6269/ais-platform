package com.example.chatservice.DTO;

import java.util.UUID;

public record AddParticipantDTO(
        UUID roomId,
        String participantEmail
) {
}

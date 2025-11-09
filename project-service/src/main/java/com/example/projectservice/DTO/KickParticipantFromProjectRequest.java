package com.example.projectservice.DTO;

import java.util.UUID;

public record KickParticipantFromProjectRequest(
        Long participantId,
        UUID projectId
) {}

package com.example.projectservice.DTO;

import com.example.projectservice.entity.Project;


import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record ProjectDTO(
        UUID id,
        String name,
        String description,
        String projectStatus,
        Long maxParticipants,
        Instant createdAt,
        Set<ParticipantDTO> participants
) {
    public static ProjectDTO toDTO(Project project) {
        return new ProjectDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getProjectStatus().name(),
                project.getMaxParticipants(),
                project.getCreatedAt(),
                project.getParticipants()
                        .stream()
                        .map(ParticipantDTO::toDTO)
                        .collect(Collectors.toSet())
        );
    }
}

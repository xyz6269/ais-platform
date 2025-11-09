package com.example.projectservice.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProjectCreationDTO(
        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotBlank
        String projectStatus,

        @NotNull
        Long maxParticipants
) {
}

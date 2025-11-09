package com.example.projectservice.DTO;


import com.example.projectservice.entity.Participant;

public record ParticipantDTO(
        Long id,
        String email
) {
    public static ParticipantDTO toDTO(Participant participant) {
        return new ParticipantDTO(participant.getId(), participant.getEmail());
    }
}
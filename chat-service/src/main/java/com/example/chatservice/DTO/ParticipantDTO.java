package com.example.chatservice.DTO;

import com.example.chatservice.entity.Participant;

public record ParticipantDTO(
        Long id,
        String email
) {
    public static ParticipantDTO toDTO(Participant participant) {
        return new ParticipantDTO(participant.getId(), participant.getEmail());
    }

    public static Participant toEntity(ParticipantDTO DTO) {
        Participant p = new Participant();
        p.setId(DTO.id);
        p.setEmail(DTO.email);
        return p;
    }
}

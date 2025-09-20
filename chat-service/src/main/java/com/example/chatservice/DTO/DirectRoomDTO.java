package com.example.chatservice.DTO;

import com.example.chatservice.entity.DirectRoom;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record DirectRoomDTO(
        UUID id,
        Instant createdAt,
        List<ParticipantDTO> participants,
        List<ChatMessageDTO> messages
) {
    public static DirectRoomDTO toDTO(DirectRoom room) {
        return  new DirectRoomDTO(room.getId(),
                room.getCreatedAt(),
                room.getParticipants().stream()
                        .map(ParticipantDTO::toDTO)
                        .toList(),
                room.getMessages().stream()
                        .map(ChatMessageDTO::toDTO)
                        .toList()
        );
    }
}

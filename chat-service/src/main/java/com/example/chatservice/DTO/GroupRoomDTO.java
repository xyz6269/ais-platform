package com.example.chatservice.DTO;

import com.example.chatservice.entity.GroupRoom;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record GroupRoomDTO(
        UUID id,
        Instant createdAt,
        String name,
        List<ParticipantDTO> participants,
        List<ParticipantDTO> admins,
        List<ChatMessageDTO> messages
) {
    public static GroupRoomDTO toDTO(GroupRoom room) {
        return  new GroupRoomDTO(room.getId(),
                room.getCreatedAt(),
                room.getName(),
                room.getParticipants().stream()
                        .map(ParticipantDTO::toDTO)
                        .toList(),
                room.getParticipants().stream()
                        .map(ParticipantDTO::toDTO)
                        .toList(),
                room.getMessages().stream()
                        .map(ChatMessageDTO::toDTO)
                        .toList()
        );

    }
}
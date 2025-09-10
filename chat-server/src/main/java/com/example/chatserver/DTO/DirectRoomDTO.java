package com.example.chatserver.DTO;

import com.example.chatserver.enums.RoomType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record DirectRoomDTO(
        UUID id,
        RoomType roomType,
        Instant createdAt,
        List<String> participants,
        List<ChatMessageDTO> messages
) {}

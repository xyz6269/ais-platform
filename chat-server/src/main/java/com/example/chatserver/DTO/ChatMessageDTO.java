package com.example.chatserver.DTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChatMessageDTO(
        UUID id,
        String senderId,
        List<String> receiversIds,
        Instant sentAt,
        String content,
        String attachmentType,
        byte[] attachmentData,
        UUID roomId
) {}

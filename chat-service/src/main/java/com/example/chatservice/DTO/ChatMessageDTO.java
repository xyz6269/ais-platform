package com.example.chatservice.DTO;

import com.example.chatservice.entity.ChatMessage;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChatMessageDTO(
        UUID id,
        ParticipantDTO sender,
        List<ParticipantDTO> receiversIds,
        Instant sentAt,
        String content,
        String attachmentType,
        byte[] attachmentData,
        UUID roomId
) {
    public static ChatMessageDTO toDTO(ChatMessage message) {
        return new ChatMessageDTO(
                message.getId(),
                ParticipantDTO.toDTO(message.getSender()),
                null,
                message.getSentAt(),
                message.getContent(),
                message.getAttachmentType().name(),
                message.getAttachmentData(),
                message.getRoom().getId()
        );
    }
}

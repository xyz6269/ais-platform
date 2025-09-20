package com.example.chatservice.service;

import com.example.chatservice.DTO.ChatMessageDTO;
import com.example.chatservice.entity.ChatMessage;
import com.example.chatservice.entity.Participant;
import com.example.chatservice.entity.Room;
import com.example.chatservice.enums.AttachmentType;
import com.example.chatservice.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final RoomService roomService;
    private final ParticipantService participantService;

    public void saveMessage(ChatMessageDTO dto) {
        log.info("saving chat message into db");
        ChatMessage message = buildMessageFromDTO(dto);
        Room room = message.getRoom();
        room.getMessages().add(message);
        chatMessageRepository.save(message);
        updateRoomAfterSavingMessage(room);
        log.info("saved message : {} to db", room.getId());
    }

    private Participant getParticipant(String email) {
        return participantService.getParticipantByEmail(email);
    }

    private Room getRoom(UUID uuid) {
        return roomService.getRoomById(uuid);
    }

    private void updateRoomAfterSavingMessage(Room room) {
        roomService.updateRoom(room);
    }

    private ChatMessage buildMessageFromDTO(ChatMessageDTO dto) {
        return ChatMessage.builder()
                .id(UUID.randomUUID())
                .sender(getParticipant(dto.sender().email()))
                .sentAt(Instant.now())
                .content(dto.content())
                .attachmentType(AttachmentType.valueOf(dto.attachmentType()))
                .attachmentData(dto.attachmentData())
                .room(getRoom(dto.roomId()))
                .build();
    }

}

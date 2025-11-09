package com.example.chatservice.service;

import com.example.chatservice.DTO.ChatMessageDTO;
import com.example.chatservice.entity.ChatMessage;
import com.example.chatservice.entity.Participant;
import com.example.chatservice.enums.AttachmentType;
import com.example.chatservice.exceptions.ChatMessageNotFoundException;
import com.example.chatservice.exceptions.PermissionException;
import com.example.chatservice.repository.ChatMessageRepository;
import com.example.chatservice.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ParticipantService participantService;
    private final DirectRoomService directRoomService;
    private final GroupRoomService groupRoomService;

    @Transactional
    public void saveDirectMessage(ChatMessageDTO dto) {
        log.info("saving direct chat message into db");
        ChatMessage message = buildMessageFromDTO(dto);
        chatMessageRepository.save(message);
        directRoomService.addMessageToDirectChatRoom(message, dto.room());
        log.info("saved direct chat message : {} to db", dto.id());
    }

    @Transactional
    public void saveGroupMessage(ChatMessageDTO dto) {
        log.info("saving group chat message into db");
        ChatMessage message = buildMessageFromDTO(dto);
        chatMessageRepository.save(message);
        groupRoomService.addMessageToGroupChatRoom(message, dto.room());
        log.info("saved group chat message : {} to db", dto.id());
    }

    @Transactional(readOnly = true)
    public ChatMessage getChatMessageById(UUID uuid) {
        log.info("fetching message of id : {}", uuid);
        return chatMessageRepository.findById(uuid).orElseThrow(() -> new ChatMessageNotFoundException("no such message with this id : "+ uuid));
    }

    @Transactional
    public void deleteMessage(UUID uuid) {
        log.info("deleting message : {} from the db",uuid);
        chatMessageRepository.deleteById(uuid);
        log.info("message");
    }

    @Transactional
    public void deleteCurrentUserChatMessage(UUID uuid) {
        ChatMessage chatMessageToDelete = getChatMessageById(uuid);
        if (!chatMessageToDelete.getSender().getEmail().equals(JwtUtil.getCurrentUserEmail())) {
            throw new PermissionException("normal users can only delete their own chat messages");
        } else deleteMessage(uuid);
    }

    @Transactional(readOnly = true)
    public Participant getParticipant(String email) {
        return participantService.getParticipantByEmail(email);
    }

    private ChatMessage buildMessageFromDTO(ChatMessageDTO dto) {
        return ChatMessage.builder()
                .id(UUID.randomUUID())
                .sender(getParticipant(dto.sender().email()))
                .sentAt(Instant.now())
                .content(dto.content())
                .attachmentType(AttachmentType.valueOf(dto.attachmentType()))
                .attachmentData(dto.attachmentData())
                .build();
    }

}

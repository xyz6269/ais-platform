package com.example.chatservice.redis;


import com.example.chatservice.DTO.ChatMessageDTO;
import com.example.chatservice.DTO.ParticipantDTO;
import com.example.chatservice.service.ChatMessageService;
import com.example.chatservice.service.ParticipantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisSubscriber {

    private final Jackson2JsonRedisSerializer<ParticipantDTO> participantSerializer;
    private final Jackson2JsonRedisSerializer<ChatMessageDTO> chatMessageSerializer;
    private final ParticipantService participantService;
    private final ChatMessageService chatMessageService;

    @Autowired
    public RedisSubscriber(ParticipantService participantService, ChatMessageService chatMessageService) {
        this.participantSerializer = new Jackson2JsonRedisSerializer<>(ParticipantDTO.class);
        this.chatMessageSerializer = new Jackson2JsonRedisSerializer<>(ChatMessageDTO.class);
        this.participantService = participantService;
        this.chatMessageService = chatMessageService;
    }

    public void handleParticipantMessage(Message message) {
        try {
            ParticipantDTO dto = participantSerializer.deserialize(message.getBody());
            participantService.createParticipant(dto);
        } catch (Exception e) {
            log.error("Failed to process message from the redis sub", e);
        }
    }

    public void handleAdminMessage(Message message) {
        try {
            ParticipantDTO dto = participantSerializer.deserialize(message.getBody());
            participantService.makeParticipantAdmin(dto.id());
        } catch (Exception e) {
            log.error("Failed to process message from the redis sub", e);
        }
    }

    public void handleDirectChatMessage(Message message) {
        try {
            ChatMessageDTO dto = chatMessageSerializer.deserialize(message.getBody());
            chatMessageService.saveDirectMessage(dto);
        } catch (Exception e) {
            log.error("Failed to process message from the redis sub", e);
        }
    }

    public void handleGroupChatMessage(Message message) {
        try {
            ChatMessageDTO dto = chatMessageSerializer.deserialize(message.getBody());
            chatMessageService.saveGroupMessage(dto);
        } catch (Exception e) {
            log.error("Failed to process message from the redis sub", e);
        }
    }

}

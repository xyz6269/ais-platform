package com.example.chatservice.config;


import com.example.chatservice.DTO.ParticipantDTO;
import com.example.chatservice.service.ParticipantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisSubscriber implements MessageListener {

    private final Jackson2JsonRedisSerializer<ParticipantDTO> serializer;
    private final ParticipantService participantService;

    @Autowired
    public RedisSubscriber(ParticipantService participantService) {
        this.serializer = new Jackson2JsonRedisSerializer<>(ParticipantDTO.class);
        this.participantService = participantService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ParticipantDTO dto = serializer.deserialize(message.getBody());
            participantService.createParticipant(dto);
        } catch (Exception e) {
            log.error("Failed to process message from the redis sub", e);
        }
    }

}

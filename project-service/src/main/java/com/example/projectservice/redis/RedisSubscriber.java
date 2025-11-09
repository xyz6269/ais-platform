package com.example.projectservice.redis;

import com.example.projectservice.DTO.ParticipantDTO;
import com.example.projectservice.service.ParticipantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final ParticipantService participantService;

    @Autowired
    public RedisSubscriber(ParticipantService participantService, ObjectMapper objectMapper) {
        this.participantService = participantService;
        this.objectMapper = objectMapper;
    }

    public void handleParticipantMessage(String messageJson) {
        try {
            log.info("Received participant message: {}", messageJson);
            ParticipantDTO dto = objectMapper.readValue(messageJson, ParticipantDTO.class);
            participantService.createParticipant(dto);
        } catch (Exception e) {
            log.error("Failed to process participant message from the redis sub. Message: {}", messageJson, e);
        }
    }

    public void handleAdminMessage(String messageJson) {
        try {
            log.info("Received admin promotion message: {}", messageJson);
            ParticipantDTO dto = objectMapper.readValue(messageJson, ParticipantDTO.class);
            participantService.makeParticipantAdmin(dto.id());
        } catch (Exception e) {
            log.error("Failed to process admin message from the redis sub. Message: {}", messageJson, e);
        }
    }
}
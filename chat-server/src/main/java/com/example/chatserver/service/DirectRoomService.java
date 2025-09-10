package com.example.chatserver.service;

import com.example.chatserver.DTO.DirectRoomDTO;
import com.example.chatserver.entity.DirectRoom;
import com.example.chatserver.mapper.DirectRoomMapper;
import com.example.chatserver.repository.DirectRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectRoomService {

    private final DirectRoomRepository directRoomRepository;

    public void createRoom(DirectRoomDTO dto) {
        log.info("Creating direct messaging room");
        DirectRoom room = DirectRoomMapper.INSTANCE.toEntity(dto);
        room.setId(UUID.randomUUID());
        room.setCreatedAt(Instant.now());
        try {
            directRoomRepository.save(room);
            log.debug("Direct messaging room created successfully : {}", room.getId());
        } catch (Exception e) {
            log.error("Direct messaging room creation failed : {}", e.getMessage());
        }

    }
}

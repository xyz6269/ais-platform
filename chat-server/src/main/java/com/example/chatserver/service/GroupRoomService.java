package com.example.chatserver.service;

import com.example.chatserver.DTO.GroupRoomDTO;
import com.example.chatserver.entity.GroupRoom;
import com.example.chatserver.mapper.GroupRoomMapper;
import com.example.chatserver.repository.GroupRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupRoomService {

    private final GroupRoomRepository groupRoomRepository;

    public void createGroupRoom(GroupRoomDTO dto) {
        log.info("Creating group messaging room");
        GroupRoom room = GroupRoomMapper.INSTANCE.toEntity(dto);
        room.setId(UUID.randomUUID());
        room.setCreatedAt(Instant.now());
        try {
            groupRoomRepository.save(room);
            log.debug("Group messaging room created successfully : {}", room.getId());
        } catch (Exception e) {
            log.error("Group messaging room creation failed : {}", e.getMessage());
        }
    }
}

package com.example.chatservice.service;


import com.example.chatservice.DTO.ChatMessageDTO;
import com.example.chatservice.DTO.ParticipantDTO;
import com.example.chatservice.exceptions.RoomNotFoundException;
import com.example.chatservice.entity.Room;
import com.example.chatservice.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;


    @Transactional
    public void updateRoom(Room room) {
        roomRepository.save(room);
    }

    @Transactional(readOnly = true)
    public Room getRoomById(UUID id) {
        log.debug("Fetching room with ID: {}", id);
        return roomRepository.findRoomById(id).orElseThrow(() -> new RoomNotFoundException("no room with the given ID"));
    }

    @Transactional(readOnly = true)
    public List<ParticipantDTO> getRoomParticipants(UUID id) {
        log.debug("Fetching all participants in room : {}", id);
        return roomRepository.findParticipantByRoomId(id)
                .stream()
                .map(ParticipantDTO::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDTO> getRoomMessages(UUID id) {
        log.debug("Fetching all messages in room : {}", id);
        return getRoomById(id).getMessages()
                .stream()
                .map(ChatMessageDTO::toDTO)
                .toList();
    }





}

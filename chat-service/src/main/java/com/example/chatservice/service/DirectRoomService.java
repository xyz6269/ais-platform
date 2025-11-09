package com.example.chatservice.service;

import com.example.chatservice.DTO.DirectRoomDTO;
import com.example.chatservice.DTO.ParticipantDTO;
import com.example.chatservice.entity.ChatMessage;
import com.example.chatservice.entity.DirectRoom;
import com.example.chatservice.entity.Participant;
import com.example.chatservice.exceptions.RoomNotFoundException;
import com.example.chatservice.repository.DirectRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectRoomService {

    private final DirectRoomRepository directRoomRepository;
    private final ParticipantService participantService;

    @Transactional
    public void createDirectRoom(DirectRoomDTO dto) {
        log.info("Creating direct messaging room");
        DirectRoom room = buildDirectRoomFromDTO(dto);
        directRoomRepository.save(room);
        log.debug("Direct messaging room created successfully : {}", room.getId());
    }

    @Transactional(readOnly = true)
    public DirectRoomDTO getDirectRoom(UUID id) {
        return DirectRoomDTO.toDTO(getDirectRoomEntity(id));
    }

    @Transactional(readOnly = true)
    public List<DirectRoomDTO> getAllDirectRooms() {
        return directRoomRepository.findAll().stream()
                .map(DirectRoomDTO::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DirectRoomDTO> getRoomsWhereUserIsParticipant(String dto) {
        log.debug("Fetching all rooms where the user : {} is a participant", dto);
        return directRoomRepository.findRoomsByUserEmail(dto)
                .stream()
                .map(DirectRoomDTO::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addMessageToDirectChatRoom(ChatMessage message, UUID roomId) {
        log.debug("Fetching room : {} to add the new message to it : {} is a participant", roomId, message.getContent());
        DirectRoom room = getDirectRoomEntity(roomId);
        room.getMessages().add(message);
        updateDirectRoom(room);
    }

    @Transactional(readOnly = true)
    public DirectRoom getDirectRoomEntity(UUID uuid) {
        return directRoomRepository.findById(uuid)
                .orElseThrow(() -> new RoomNotFoundException("this room of id : "+ uuid.toString() +" doesn't exist"));
    }

    @Transactional
    public void updateDirectRoom(DirectRoom room) {
        directRoomRepository.save(room);
    }

    private List<Participant> getParticipantsFromDTO(List<String> emails) {
        return participantService.getParticipantsByEmails(emails);
    }

    private DirectRoom buildDirectRoomFromDTO(DirectRoomDTO dto) {
        return DirectRoom.builder()
                .id(UUID.randomUUID())
                .createdAt(Instant.now())
                .participants(getParticipantsFromDTO(dto.participants().stream()
                        .map(ParticipantDTO::email)
                        .toList()))
                .build();
    }

}

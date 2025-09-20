package com.example.chatservice.service;

import com.example.chatservice.DTO.DirectRoomDTO;
import com.example.chatservice.DTO.ParticipantDTO;
import com.example.chatservice.entity.DirectRoom;
import com.example.chatservice.entity.Participant;
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
    private final RoomService roomService;

    @Transactional
    public void createDirectRoom(DirectRoomDTO dto) {
        log.info("Creating direct messaging room");
        DirectRoom room = buildDirectRoomFromDTO(dto);
        directRoomRepository.save(room);
        log.debug("Direct messaging room created successfully : {}", room.getId());
    }

    @Transactional(readOnly = true)
    public Participant getParticipant(String email) {
        return participantService.getParticipantByEmail(email);
    }

    @Transactional(readOnly = true)
    public DirectRoom getDirectRoom(UUID id) {
        return (DirectRoom) roomService.getRoomById(id);
    }

    @Transactional(readOnly = true)
    public List<DirectRoomDTO> getAllDirectRooms() {
        return directRoomRepository.findAll().stream()
                .map(DirectRoomDTO::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DirectRoomDTO> getRoomsWhereUserIsParticipant(String email) {
        log.debug("Fetching all rooms where the user : {} is a participant", email);
        return directRoomRepository.findRoomsByUserEmail(email)
                .stream()
                .map(DirectRoomDTO::toDTO)
                .collect(Collectors.toList());
    }

    private DirectRoom buildDirectRoomFromDTO(DirectRoomDTO dto) {
        DirectRoom directRoom =  new DirectRoom();
        directRoom.setId(UUID.randomUUID());
        directRoom.setCreatedAt(Instant.now());
        directRoom.setParticipants(getParticipantsFromDTO(dto.participants().stream()
                .map(ParticipantDTO::email)
                .toList()));
        return directRoom;
    }

    private List<Participant> getParticipantsFromDTO(List<String> emails) {
        return participantService.getParticipantsByEmails(emails);
    }

}

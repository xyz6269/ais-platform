package com.example.chatservice.service;

import com.example.chatservice.DTO.AddParticipantDTO;
import com.example.chatservice.DTO.ParticipantDTO;
import com.example.chatservice.DTO.GroupRoomDTO;
import com.example.chatservice.entity.GroupRoom;
import com.example.chatservice.entity.Participant;
import com.example.chatservice.exceptions.RoomNotFoundException;
import com.example.chatservice.repository.GroupRoomRepository;
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
public class GroupRoomService {

    private final GroupRoomRepository groupRoomRepository;
    private final ParticipantService participantService;

    @Transactional
    public void createGroupRoom(GroupRoomDTO dto) {
        log.info("Creating group messaging room");
        GroupRoom room = buildDirectRoomFromDTO(dto);
        groupRoomRepository.save(room);
        log.debug("Group messaging room created successfully: {}", room.getId());
    }

    @Transactional
    public void deleteGroupRoom(UUID id) {
        log.debug("deleting room : {}", id);
        groupRoomRepository.deleteById(id);
    }

    @Transactional
    public void updateGroupRoom(GroupRoom room) {
        log.debug("updating room : {}", room.getId());
        groupRoomRepository.save(room);
    }

    @Transactional(readOnly = true)
    public List<GroupRoomDTO> getAllDirectRooms() {
        return groupRoomRepository.findAll().stream()
                .map(GroupRoomDTO::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GroupRoomDTO> getRoomsWhereUserIsParticipant(String email) {
        log.debug("Fetching all rooms where the user : {} is a participant", email);
        return groupRoomRepository.findRoomsByUserEmail(email)
                .stream()
                .map(GroupRoomDTO::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addParticipantsToGroupRoom(AddParticipantDTO dto) {
        log.debug("fetching group : {} to add the user to it", dto.roomId());
        GroupRoom groupRoom = groupRoomRepository.findGroupRoomById(dto.roomId()).orElseThrow(() -> new RoomNotFoundException("Room with id : "+ dto.roomId().toString() +" wasn't found"));
        log.debug("fetching participant : {} to add to user", dto.participantEmail());
        Participant participantToAdd = participantService.getParticipantByEmail(dto.participantEmail());
        log.debug("adding user : {} to the participants of group : {} ", dto.participantEmail(), dto.roomId());
        groupRoom.getParticipants().add(participantToAdd);
        updateGroupRoom(groupRoom);
    }


    private GroupRoom buildDirectRoomFromDTO(GroupRoomDTO dto) {
        GroupRoom groupRoom =  new GroupRoom();
        groupRoom.setId(UUID.randomUUID());
        groupRoom.setCreatedAt(Instant.now());
        groupRoom.setName(dto.name());
        groupRoom.setParticipants(getParticipantsFromDTO(dto.participants().stream()
                .map(ParticipantDTO::email)
                .toList()));
        groupRoom.setAdmins(getParticipantsFromDTO(dto.participants().stream()
                .map(ParticipantDTO::email)
                .toList()));
        return groupRoom;
    }

    private List<Participant> getParticipantsFromDTO(List<String> emails) {
        return participantService.getParticipantsByEmails(emails);
    }

}

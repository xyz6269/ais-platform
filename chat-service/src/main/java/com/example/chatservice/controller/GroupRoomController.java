package com.example.chatservice.controller;

import com.example.chatservice.DTO.AddParticipantDTO;
import com.example.chatservice.DTO.GroupRoomDTO;
import com.example.chatservice.service.GroupRoomService;
import com.example.chatservice.service.ParticipantService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chat/group-room")
@RequiredArgsConstructor
public class GroupRoomController {

    private final GroupRoomService groupRoomService;

    @PostMapping("/create-room")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> createRoom(@Valid @RequestBody GroupRoomDTO dto) {
        groupRoomService.createGroupRoom(dto);
        return ResponseEntity.ok("group chat room : " + dto.id() + " has been created");
    }

    @DeleteMapping("/delete-room/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> deleteRoom(@PathVariable UUID id) {
        groupRoomService.deleteGroupRoom(id);
        return ResponseEntity.ok("group chat room : " + id + " has been deleted");
    }

    @PutMapping("/add-participant-to-room")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> addParticipantsToGroupRoom(@Valid @RequestBody AddParticipantDTO dto) {
        groupRoomService.addParticipantsToGroupRoom(dto);
        return ResponseEntity.ok("user : " + dto.participantEmail() + " has been added to the participants of group : " + dto.roomId());

    }

    @GetMapping("/all")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<GroupRoomDTO>> getAllGroupRooms() {
        return ResponseEntity.ok(groupRoomService.getAllDirectRooms());
    }

    @GetMapping("/all-participant-rooms")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<GroupRoomDTO>> getAllParticipantRooms() {
        return ResponseEntity.ok(groupRoomService.getRoomsWhereUserIsParticipant(ParticipantService.getCurrentUser()));
    }

}

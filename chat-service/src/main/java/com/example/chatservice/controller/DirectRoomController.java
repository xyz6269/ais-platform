package com.example.chatservice.controller;

import com.example.chatservice.DTO.DirectRoomDTO;
import com.example.chatservice.security.jwt.JwtUtil;
import com.example.chatservice.service.DirectRoomService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chat/direct-room")
@RequiredArgsConstructor
public class DirectRoomController {

    private final DirectRoomService directRoomService;

    @PostMapping("/create-room")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> createRoom(@Valid @RequestBody DirectRoomDTO dto) {
        directRoomService.createDirectRoom(dto);
        return ResponseEntity.ok("Direct chat room : " + dto.id() + " has been created");
    }

    @GetMapping("/all")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<DirectRoomDTO>> getAllDirectRooms() {
        return ResponseEntity.ok(directRoomService.getAllDirectRooms());
    }

    @GetMapping("/get-room/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<DirectRoomDTO> getDirectRoomById(@PathVariable UUID id) {
       return ResponseEntity.ok(directRoomService.getDirectRoom(id));
    }

    @GetMapping("/get-participant-rooms")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<DirectRoomDTO>> getDirectRoomWhereUserIsParticipant() {
        return ResponseEntity.ok(directRoomService.getRoomsWhereUserIsParticipant(JwtUtil.getCurrentUserEmail()));
    }



}

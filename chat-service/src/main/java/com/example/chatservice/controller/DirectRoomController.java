package com.example.chatservice.controller;

import com.example.chatservice.DTO.DirectRoomDTO;
import com.example.chatservice.service.DirectRoomService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat/direct-room")
@RequiredArgsConstructor
public class DirectRoomController {

    private final DirectRoomService directRoomService;

    @PostMapping("/create-room")
    @SecurityRequirement(name = "bearerAuth")
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



}

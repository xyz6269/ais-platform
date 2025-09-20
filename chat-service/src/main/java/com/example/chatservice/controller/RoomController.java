package com.example.chatservice.controller;

import com.example.chatservice.DTO.ChatMessageDTO;
import com.example.chatservice.DTO.ParticipantDTO;
import com.example.chatservice.service.RoomService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chat/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;


    @GetMapping("/participants/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<ParticipantDTO>> getRoomParticipants(@PathVariable UUID id) {
        return ResponseEntity.ok(roomService.getRoomParticipants(id));
    }

    @GetMapping("/messages/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<ChatMessageDTO>> getRoomMessages(@PathVariable UUID id) {
        return ResponseEntity.ok(roomService.getRoomMessages(id));
    }
}

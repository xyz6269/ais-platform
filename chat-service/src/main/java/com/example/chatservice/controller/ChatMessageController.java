package com.example.chatservice.controller;

import com.example.chatservice.service.ChatMessageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chat/chat-message")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @DeleteMapping("/delete-message/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteChatMessage(@PathVariable UUID uuid) {
        chatMessageService.deleteMessage(uuid);
        return ResponseEntity.ok("admin deleted message of id : " + uuid.toString());
    }

    @DeleteMapping("/delete-my-message/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> deleteMyChatMessage(@PathVariable UUID uuid) {
        chatMessageService.deleteCurrentUserChatMessage(uuid);
        return ResponseEntity.ok("user deleted their message of id : " + uuid.toString());
    }

}

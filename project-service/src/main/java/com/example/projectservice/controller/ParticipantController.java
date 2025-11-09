package com.example.projectservice.controller;

import com.example.projectservice.service.ParticipantService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/project/participant")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @DeleteMapping("/remove-task/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteParticipant(@PathVariable Long id) {
        participantService.deleteParticipant(id);
        return ResponseEntity.ok("admin removed participant : " + id);
    }
}

package com.example.authservice.controller;

import com.example.authservice.DTO.*;
import com.example.authservice.service.MemberService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginDTO request) {
        return ResponseEntity.ok(new ApiResponse<>("Login Successful", memberService.authenticateUser(request)));
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> signup(@Valid @RequestBody SignUpDTO request) {
        return new ApiResponse<>("user registered successfully", memberService.registerUser(request));
    }

    @PutMapping("/activate-account/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<String> activateAccount(@PathVariable Long id) {
        return new ApiResponse<>("user : {} account's has been activated", memberService.activateUserAccount(id));
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<String>> currentAdmin() {
        return ResponseEntity.ok(new ApiResponse<String>("current user email is: ", memberService.getCurrentUser()));
    }
}

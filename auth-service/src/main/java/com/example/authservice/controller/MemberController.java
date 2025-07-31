package com.example.authservice.controller;

import com.example.authservice.DTO.ApiResponse;
import com.example.authservice.DTO.LoginDTO;
import com.example.authservice.DTO.LoginResponse;
import com.example.authservice.DTO.MemberDTO;
import com.example.authservice.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginDTO request) {
        return ResponseEntity.ok(new ApiResponse<>("Login successful", memberService.authenticateUser(request)));
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> signup(@Valid @RequestBody MemberDTO request) {
        return new ApiResponse<>("user registered successfully", memberService.registerUser(request));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<String>> currentAdmin() {
        return ResponseEntity.ok(new ApiResponse<String>("current user email is: ", memberService.getCurrentUser()));
    }
}

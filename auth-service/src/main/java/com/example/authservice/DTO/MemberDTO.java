package com.example.authservice.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberDTO(

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        @Size(max = 100)
        String email,

        @NotBlank
        @Size(max = 50)
        String firstName,

        @NotBlank
        @Size(max = 50)
        String lastName,

        @NotBlank
        @Size(max = 50)
        String phoneNumber,

        @NotBlank
        @Size(max = 50)
        String gender,

        @NotBlank
        @Size(max = 50)
        String major,

        @NotBlank(message = "Password is required")
        @Size(max = 255)
        String password
) {}

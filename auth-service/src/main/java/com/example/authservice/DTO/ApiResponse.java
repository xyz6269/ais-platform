package com.example.authservice.DTO;

public record ApiResponse<T>(
        String message,
        T data
) {}
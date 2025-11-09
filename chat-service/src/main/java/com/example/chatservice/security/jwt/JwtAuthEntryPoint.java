package com.example.chatservice.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@Component
@Slf4j
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {

        int statusCode;
        log.error("Authentication failed for request {}: {}", request.getRequestURI(), authException.getMessage(), authException);

        if (authException instanceof BadCredentialsException) {
            statusCode = HttpStatus.UNAUTHORIZED.value(); // 401
        } else if (authException instanceof DisabledException
                || authException instanceof LockedException
                || authException instanceof AccountExpiredException
                || authException instanceof CredentialsExpiredException) {
            statusCode = HttpStatus.FORBIDDEN.value(); // 403
        } else if (authException instanceof InsufficientAuthenticationException) {
            statusCode = HttpStatus.UNAUTHORIZED.value(); // 401
        } else {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value(); // 500 fallback
        }

        response.setStatus(statusCode);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> errorResponse = Map.of(
                "timestamp", Instant.now().toString(),
                "statusCode", statusCode,
                "error", HttpStatus.valueOf(statusCode).getReasonPhrase(),
                "message", authException.getMessage(),
                "path", request.getRequestURI()
        );


        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}

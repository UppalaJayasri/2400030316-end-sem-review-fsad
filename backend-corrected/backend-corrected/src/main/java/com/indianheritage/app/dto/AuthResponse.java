package com.indianheritage.app.dto;

public record AuthResponse(
    String token,
    String name,
    String email,
    String role,
    String message
) {
}

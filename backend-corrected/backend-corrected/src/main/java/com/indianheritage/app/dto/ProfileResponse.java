package com.indianheritage.app.dto;

import java.time.LocalDateTime;

public record ProfileResponse(
    Long id,
    String name,
    String email,
    String role,
    String mobileNumber,
    String gender,
    LocalDateTime createdAt
) {
}

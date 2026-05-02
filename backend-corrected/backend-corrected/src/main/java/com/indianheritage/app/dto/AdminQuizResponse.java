package com.indianheritage.app.dto;

import java.time.LocalDateTime;

public record AdminQuizResponse(
    String id,
    String title,
    boolean visible,
    int questionCount,
    int attemptCount,
    LocalDateTime createdAt,
    LocalDateTime startsAt,
    LocalDateTime expiresAt,
    boolean expired
) {
}

package com.indianheritage.app.dto;

import java.time.LocalDateTime;

public record QuizResponse(
    String id,
    String title,
    int questionCount,
    LocalDateTime startsAt,
    LocalDateTime expiresAt,
    boolean alreadyAttempted,
    Integer userScore,
    Integer totalQuestions
) {
}

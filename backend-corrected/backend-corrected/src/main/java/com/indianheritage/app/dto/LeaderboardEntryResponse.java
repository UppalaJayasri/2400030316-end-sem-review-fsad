package com.indianheritage.app.dto;

import java.time.LocalDateTime;

public record LeaderboardEntryResponse(
    String userName,
    int score,
    int totalQuestions,
    LocalDateTime completedAt
) {
}

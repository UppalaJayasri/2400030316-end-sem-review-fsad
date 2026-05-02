package com.indianheritage.app.dto;

public record AdminStatsResponse(
    long totalUsers,
    long totalPlaces,
    long totalQuizzes
) {
}

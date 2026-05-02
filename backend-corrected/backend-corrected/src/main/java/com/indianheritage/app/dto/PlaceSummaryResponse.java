package com.indianheritage.app.dto;

public record PlaceSummaryResponse(
    Long id,
    String name,
    String city,
    String state,
    String imageUrl,
    String description
) {
}

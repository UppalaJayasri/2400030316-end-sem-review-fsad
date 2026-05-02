package com.indianheritage.app.dto;

public record PlaceDetailResponse(
    Long id,
    String name,
    String city,
    String state,
    String description,
    String location,
    String timings,
    String entryFee,
    String imageUrl
) {
}

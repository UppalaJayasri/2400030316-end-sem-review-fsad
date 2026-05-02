package com.indianheritage.app.controller;

import com.indianheritage.app.dto.PlaceDetailResponse;
import com.indianheritage.app.dto.PlaceSummaryResponse;
import com.indianheritage.app.service.AuthService;
import com.indianheritage.app.service.PlaceService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

    private final PlaceService placeService;
    private final AuthService authService;

    public PlaceController(PlaceService placeService, AuthService authService) {
        this.placeService = placeService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<List<PlaceSummaryResponse>> getPlaces() {
        return ResponseEntity.ok(placeService.getPlaceSummaries());
    }

    @GetMapping("/{placeId}/details")
    public ResponseEntity<PlaceDetailResponse> getPlaceDetails(
        @PathVariable Long placeId,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        authService.requireAuthenticatedUser(authHeader);
        return ResponseEntity.ok(placeService.getPlaceDetails(placeId));
    }
}

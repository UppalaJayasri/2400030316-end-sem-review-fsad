package com.indianheritage.app.controller;

import com.indianheritage.app.dto.PlaceDetailResponse;
import com.indianheritage.app.dto.PlaceUpsertRequest;
import com.indianheritage.app.service.AuthService;
import com.indianheritage.app.service.PlaceService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/places")
public class AdminPlaceController {

    private final PlaceService placeService;
    private final AuthService authService;

    public AdminPlaceController(PlaceService placeService, AuthService authService) {
        this.placeService = placeService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<List<PlaceDetailResponse>> getAllPlaces(
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        authService.requireAdminUser(authHeader);
        return ResponseEntity.ok(placeService.getAllPlaceDetailsForAdmin());
    }

    @PostMapping
    public ResponseEntity<PlaceDetailResponse> createPlace(
        @Valid @RequestBody PlaceUpsertRequest request,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        authService.requireAdminUser(authHeader);
        return ResponseEntity.ok(placeService.createPlace(request));
    }

    @PutMapping("/{placeId}")
    public ResponseEntity<PlaceDetailResponse> updatePlace(
        @PathVariable Long placeId,
        @Valid @RequestBody PlaceUpsertRequest request,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        authService.requireAdminUser(authHeader);
        return ResponseEntity.ok(placeService.updatePlace(placeId, request));
    }

    @DeleteMapping("/{placeId}")
    public ResponseEntity<Void> deletePlace(
        @PathVariable Long placeId,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        authService.requireAdminUser(authHeader);
        placeService.deletePlace(placeId);
        return ResponseEntity.noContent().build();
    }
}

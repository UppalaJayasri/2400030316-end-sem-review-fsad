package com.indianheritage.app.controller;

import com.indianheritage.app.dto.ProfileResponse;
import com.indianheritage.app.dto.ProfileUpdateRequest;
import com.indianheritage.app.entity.User;
import com.indianheritage.app.entity.UserRole;
import com.indianheritage.app.repository.UserRepository;
import com.indianheritage.app.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public ProfileController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        User user = authService.requireAuthenticatedUser(authHeader);
        return ResponseEntity.ok(toResponse(user));
    }

    @PutMapping
    public ResponseEntity<ProfileResponse> updateProfile(
        @RequestBody ProfileUpdateRequest request,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        User user = authService.requireAuthenticatedUser(authHeader);

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName().trim());
        }

        if (request.getMobileNumber() != null) {
            user.setMobileNumber(request.getMobileNumber().trim());
        }

        if (request.getGender() != null) {
            user.setGender(request.getGender().trim());
        }

        // Only admin can update email
        if (user.getRole() == UserRole.ADMIN && request.getEmail() != null && !request.getEmail().isBlank()) {
            user.setEmail(request.getEmail().trim().toLowerCase());
        }

        User saved = userRepository.save(user);
        return ResponseEntity.ok(toResponse(saved));
    }

    private ProfileResponse toResponse(User user) {
        return new ProfileResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole().name(),
            user.getMobileNumber(),
            user.getGender(),
            user.getCreatedAt()
        );
    }
}

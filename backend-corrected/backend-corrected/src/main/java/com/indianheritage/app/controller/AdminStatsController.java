package com.indianheritage.app.controller;

import com.indianheritage.app.dto.AdminStatsResponse;
import com.indianheritage.app.repository.PlaceRepository;
import com.indianheritage.app.repository.QuizRepository;
import com.indianheritage.app.entity.UserRole;
import com.indianheritage.app.repository.UserRepository;
import com.indianheritage.app.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminStatsController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final QuizRepository quizRepository;

    public AdminStatsController(AuthService authService,
                                UserRepository userRepository,
                                PlaceRepository placeRepository,
                                QuizRepository quizRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
        this.quizRepository = quizRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsResponse> getStats(
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        authService.requireAdminUser(authHeader);
        return ResponseEntity.ok(new AdminStatsResponse(
            userRepository.countByRoleNot(UserRole.ADMIN),
            placeRepository.count(),
            quizRepository.count()
        ));
    }
}

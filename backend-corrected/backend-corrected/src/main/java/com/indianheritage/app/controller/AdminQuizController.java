package com.indianheritage.app.controller;

import com.indianheritage.app.dto.AdminQuizQuestionResponse;
import com.indianheritage.app.dto.AdminQuizResponse;
import com.indianheritage.app.dto.LeaderboardEntryResponse;
import com.indianheritage.app.dto.QuizCreateRequest;
import com.indianheritage.app.service.AuthService;
import com.indianheritage.app.service.QuizService;
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
@RequestMapping("/api/admin/quiz")
public class AdminQuizController {

    private final QuizService quizService;
    private final AuthService authService;

    public AdminQuizController(QuizService quizService, AuthService authService) {
        this.quizService = quizService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<AdminQuizResponse> createQuiz(
        @Valid @RequestBody QuizCreateRequest request,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        authService.requireAdminUser(authHeader);
        return ResponseEntity.ok(quizService.createQuiz(request));
    }

    @GetMapping
    public ResponseEntity<List<AdminQuizResponse>> getAllQuizzes(
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        authService.requireAdminUser(authHeader);
        return ResponseEntity.ok(quizService.getAllQuizzes());
    }

    @PutMapping("/{quizId}/toggle-visibility")
    public ResponseEntity<AdminQuizResponse> toggleVisibility(
        @PathVariable String quizId,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        authService.requireAdminUser(authHeader);
        return ResponseEntity.ok(quizService.toggleVisibility(quizId));
    }

    @PutMapping("/{quizId}")
    public ResponseEntity<AdminQuizResponse> updateQuiz(
        @PathVariable String quizId,
        @Valid @RequestBody QuizCreateRequest request,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        authService.requireAdminUser(authHeader);
        return ResponseEntity.ok(quizService.updateQuiz(quizId, request));
    }

    @GetMapping("/{quizId}/questions")
    public ResponseEntity<List<AdminQuizQuestionResponse>> getQuizQuestions(
        @PathVariable String quizId,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        authService.requireAdminUser(authHeader);
        return ResponseEntity.ok(quizService.getAdminQuizQuestions(quizId));
    }

    @DeleteMapping("/{quizId}")
    public ResponseEntity<Void> deleteQuiz(
        @PathVariable String quizId,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        authService.requireAdminUser(authHeader);
        quizService.deleteQuiz(quizId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{quizId}/leaderboard")
    public ResponseEntity<List<LeaderboardEntryResponse>> getLeaderboard(
        @PathVariable String quizId,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        authService.requireAdminUser(authHeader);
        return ResponseEntity.ok(quizService.getLeaderboard(quizId));
    }
}

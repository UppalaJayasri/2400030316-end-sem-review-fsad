package com.indianheritage.app.controller;

import com.indianheritage.app.dto.LeaderboardEntryResponse;
import com.indianheritage.app.dto.QuizQuestionResponse;
import com.indianheritage.app.dto.QuizResponse;
import com.indianheritage.app.dto.QuizResultResponse;
import com.indianheritage.app.dto.QuizSubmitRequest;
import com.indianheritage.app.entity.User;
import com.indianheritage.app.service.AuthService;
import com.indianheritage.app.service.QuizService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizService quizService;
    private final AuthService authService;

    public QuizController(QuizService quizService, AuthService authService) {
        this.quizService = quizService;
        this.authService = authService;
    }

    @GetMapping("/active")
    public ResponseEntity<List<QuizResponse>> getActiveQuizzes(
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        User user = authService.requireAuthenticatedUser(authHeader);
        return ResponseEntity.ok(quizService.getActiveQuizzesForUser(user));
    }

    @GetMapping("/{quizId}/questions")
    public ResponseEntity<List<QuizQuestionResponse>> getQuizQuestions(
        @PathVariable String quizId,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        User user = authService.requireAuthenticatedUser(authHeader);
        return ResponseEntity.ok(quizService.getQuizQuestions(quizId, user));
    }

    @PostMapping("/{quizId}/submit")
    public ResponseEntity<QuizResultResponse> submitQuiz(
        @PathVariable String quizId,
        @Valid @RequestBody QuizSubmitRequest request,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        User user = authService.requireAuthenticatedUser(authHeader);
        return ResponseEntity.ok(quizService.submitAttempt(quizId, user, request));
    }

    @GetMapping("/{quizId}/review")
    public ResponseEntity<QuizResultResponse> getQuizReview(
        @PathVariable String quizId,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        User user = authService.requireAuthenticatedUser(authHeader);
        return ResponseEntity.ok(quizService.getAttemptReview(quizId, user));
    }

    @GetMapping("/{quizId}/leaderboard")
    public ResponseEntity<List<LeaderboardEntryResponse>> getLeaderboard(
        @PathVariable String quizId,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        authService.requireAuthenticatedUser(authHeader);
        return ResponseEntity.ok(quizService.getLeaderboard(quizId));
    }
}

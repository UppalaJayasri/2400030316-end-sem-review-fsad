package com.indianheritage.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indianheritage.app.dto.AdminQuizQuestionResponse;
import com.indianheritage.app.dto.AdminQuizResponse;
import com.indianheritage.app.dto.LeaderboardEntryResponse;
import com.indianheritage.app.dto.QuizCreateRequest;
import com.indianheritage.app.dto.QuizQuestionResponse;
import com.indianheritage.app.dto.QuizResponse;
import com.indianheritage.app.dto.QuizResultResponse;
import com.indianheritage.app.dto.QuizSubmitRequest;
import com.indianheritage.app.entity.Quiz;
import com.indianheritage.app.entity.QuizAttempt;
import com.indianheritage.app.entity.QuizQuestion;
import com.indianheritage.app.entity.User;
import com.indianheritage.app.exception.NotFoundException;
import com.indianheritage.app.repository.QuizAttemptRepository;
import com.indianheritage.app.repository.QuizQuestionRepository;
import com.indianheritage.app.repository.QuizRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final ObjectMapper objectMapper;

    public QuizService(QuizRepository quizRepository,
                       QuizQuestionRepository quizQuestionRepository,
                       QuizAttemptRepository quizAttemptRepository,
                       ObjectMapper objectMapper) {
        this.quizRepository = quizRepository;
        this.quizQuestionRepository = quizQuestionRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.objectMapper = objectMapper;
    }

    // ─── Admin operations ────────────────────────────────────────────

    @Transactional
    public AdminQuizResponse createQuiz(QuizCreateRequest request) {
        Quiz quiz = new Quiz(request.getTitle().trim());
        quiz.setQuestionCount(request.getQuestionCount());
        quiz.setStartsAt(request.getStartsAt());
        quiz.setExpiresAt(request.getEndsAt());
        quiz.setVisible(request.getVisible());
        
        quiz = quizRepository.save(quiz);

        for (QuizCreateRequest.QuestionPayload qp : request.getQuestions()) {
            QuizQuestion question = new QuizQuestion(
                quiz,
                qp.getQuestionText().trim(),
                qp.getOptionA().trim(),
                qp.getOptionB().trim(),
                qp.getOptionC().trim(),
                qp.getOptionD().trim(),
                qp.getCorrectOption(),
                qp.getExplanation() != null ? qp.getExplanation().trim() : ""
            );
            quizQuestionRepository.save(question);
        }

        return toAdminResponse(quiz, request.getQuestions().size(), 0);
    }

    @Transactional(readOnly = true)
    public List<AdminQuizResponse> getAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findAllByOrderByCreatedAtDesc();
        return quizzes.stream()
            .map(quiz -> toAdminResponse(
                quiz,
                quiz.getQuestions().size(),
                quiz.getAttempts().size()
            ))
            .toList();
    }

    @Transactional
    public AdminQuizResponse toggleVisibility(String quizId) {
        Quiz quiz = quizRepository.findById(quizId)
            .orElseThrow(() -> new NotFoundException("Quiz not found"));
        quiz.setVisible(!quiz.isVisible());
        quizRepository.save(quiz);
        return toAdminResponse(quiz, quiz.getQuestions().size(), quiz.getAttempts().size());
    }

    @Transactional
    public void deleteQuiz(String quizId) {
        Quiz quiz = quizRepository.findById(quizId)
            .orElseThrow(() -> new NotFoundException("Quiz not found"));
        quizRepository.delete(quiz);
    }

    @Transactional
    public AdminQuizResponse updateQuiz(String quizId, QuizCreateRequest request) {
        Quiz quiz = quizRepository.findById(quizId)
            .orElseThrow(() -> new NotFoundException("Quiz not found"));

        quiz.setTitle(request.getTitle().trim());
        quiz.setQuestionCount(request.getQuestionCount());
        quiz.setStartsAt(request.getStartsAt());
        quiz.setExpiresAt(request.getEndsAt());
        quiz.setVisible(request.getVisible());

        // Remove old questions and add new ones
        quizQuestionRepository.deleteByQuiz_Id(quizId);

        for (QuizCreateRequest.QuestionPayload qp : request.getQuestions()) {
            QuizQuestion question = new QuizQuestion(
                quiz,
                qp.getQuestionText().trim(),
                qp.getOptionA().trim(),
                qp.getOptionB().trim(),
                qp.getOptionC().trim(),
                qp.getOptionD().trim(),
                qp.getCorrectOption(),
                qp.getExplanation() != null ? qp.getExplanation().trim() : ""
            );
            quizQuestionRepository.save(question);
        }

        quiz = quizRepository.save(quiz);
        return toAdminResponse(quiz, request.getQuestions().size(), quiz.getAttempts().size());
    }

    @Transactional(readOnly = true)
    public List<AdminQuizQuestionResponse> getAdminQuizQuestions(String quizId) {
        quizRepository.findById(quizId)
            .orElseThrow(() -> new NotFoundException("Quiz not found"));

        return quizQuestionRepository.findByQuiz_IdOrderByIdAsc(quizId).stream()
            .map(q -> new AdminQuizQuestionResponse(
                q.getId(),
                q.getQuestionText(),
                q.getOptionA(),
                q.getOptionB(),
                q.getOptionC(),
                q.getOptionD(),
                q.getCorrectOption(),
                q.getExplanation()
            ))
            .toList();
    }

    // ─── User operations ─────────────────────────────────────────────

    public List<QuizResponse> getActiveQuizzesForUser(User user) {
        LocalDateTime now = LocalDateTime.now();
        List<Quiz> quizzes = quizRepository.findActiveQuizzesForUser(now);
        
        return quizzes.stream().map(quiz -> {
            int questionCount = (int) quizQuestionRepository.countByQuiz_Id(quiz.getId());
            Optional<QuizAttempt> attempt = quizAttemptRepository
                .findByQuiz_IdAndUserName(quiz.getId(), user.getEmail());

            return new QuizResponse(
                quiz.getId(),
                quiz.getTitle(),
                questionCount,
                quiz.getStartsAt(),
                quiz.getExpiresAt(),
                attempt.isPresent(),
                attempt.map(QuizAttempt::getScore).orElse(null),
                attempt.map(QuizAttempt::getTotalQuestions).orElse(null)
            );
        }).toList();
    }

    public List<QuizQuestionResponse> getQuizQuestions(String quizId, User user) {
        Quiz quiz = quizRepository.findById(quizId)
            .orElseThrow(() -> new NotFoundException("Quiz not found"));

        if (!quiz.isActiveForUsers()) {
            throw new IllegalArgumentException("This quiz is not currently available");
        }

        if (quizAttemptRepository.existsByQuiz_IdAndUserName(quizId, user.getEmail())) {
            throw new IllegalArgumentException("You have already completed this quiz");
        }

        return quizQuestionRepository.findByQuiz_IdOrderByIdAsc(quizId).stream()
            .map(q -> new QuizQuestionResponse(
                q.getId(),
                q.getQuestionText(),
                q.getOptionA(),
                q.getOptionB(),
                q.getOptionC(),
                q.getOptionD()
            ))
            .toList();
    }

    @Transactional
    public QuizResultResponse submitAttempt(String quizId, User user, QuizSubmitRequest request) {
        Quiz quiz = quizRepository.findById(quizId)
            .orElseThrow(() -> new NotFoundException("Quiz not found"));

        if (!quiz.isActiveForUsers()) {
            throw new IllegalArgumentException("This quiz is not currently available");
        }

        if (quizAttemptRepository.existsByQuiz_IdAndUserName(quizId, user.getEmail())) {
            throw new IllegalArgumentException("You have already completed this quiz");
        }

        List<QuizQuestion> questions = quizQuestionRepository.findByQuiz_IdOrderByIdAsc(quizId);
        List<Integer> answers = request.getAnswers();

        if (answers.size() != questions.size()) {
            throw new IllegalArgumentException("Expected " + questions.size() + " answers but got " + answers.size());
        }

        int score = 0;
        List<QuizResultResponse.AnswerDetail> details = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            QuizQuestion q = questions.get(i);
            int userAnswer = answers.get(i);
            boolean correct = userAnswer == q.getCorrectOption();
            if (correct) score++;

            details.add(new QuizResultResponse.AnswerDetail(
                q.getQuestionText(), q.getOptionA(), q.getOptionB(), q.getOptionC(), q.getOptionD(),
                userAnswer, q.getCorrectOption(), correct, q.getExplanation()
            ));
        }

        String answersJson = serializeAnswers(answers);
        QuizAttempt attempt = new QuizAttempt(quiz, user.getEmail(), score, questions.size(), answersJson);
        quizAttemptRepository.save(attempt);

        return new QuizResultResponse(score, questions.size(), details);
    }

    @Transactional(readOnly = true)
    public QuizResultResponse getAttemptReview(String quizId, User user) {
        QuizAttempt attempt = quizAttemptRepository.findByQuiz_IdAndUserName(quizId, user.getEmail())
            .orElseThrow(() -> new NotFoundException("Quiz review not found"));

        List<Integer> userAnswers = deserializeAnswers(attempt.getAnswersJson());
        List<QuizQuestion> questions = quizQuestionRepository.findByQuiz_IdOrderByIdAsc(quizId);

        List<QuizResultResponse.AnswerDetail> details = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            QuizQuestion q = questions.get(i);
            int userAnswer = i < userAnswers.size() ? userAnswers.get(i) : -1;
            boolean correct = userAnswer == q.getCorrectOption();
            details.add(new QuizResultResponse.AnswerDetail(
                q.getQuestionText(), q.getOptionA(), q.getOptionB(), q.getOptionC(), q.getOptionD(),
                userAnswer, q.getCorrectOption(), correct, q.getExplanation()
            ));
        }

        return new QuizResultResponse(attempt.getScore(), attempt.getTotalQuestions(), details);
    }

    public List<LeaderboardEntryResponse> getLeaderboard(String quizId) {
        return quizAttemptRepository.findByQuiz_IdOrderByScoreDescCompletedAtAsc(quizId).stream()
            .map(a -> new LeaderboardEntryResponse(
                a.getUserName(),
                a.getScore(),
                a.getTotalQuestions(),
                a.getCompletedAt()
            ))
            .toList();
    }

    // ─── Helpers ─────────────────────────────────────────────────────

    private AdminQuizResponse toAdminResponse(Quiz quiz, int questionCount, int attemptCount) {
        return new AdminQuizResponse(
            quiz.getId(),
            quiz.getTitle(),
            quiz.isVisible(),
            questionCount,
            attemptCount,
            quiz.getCreatedAt(),
            quiz.getStartsAt(),
            quiz.getExpiresAt(),
            quiz.isExpired()
        );
    }

    private String serializeAnswers(List<Integer> answers) {
        try {
            return objectMapper.writeValueAsString(answers);
        } catch (JsonProcessingException ex) {
            return "[]";
        }
    }

    private List<Integer> deserializeAnswers(String answersJson) {
        if (answersJson == null || answersJson.isBlank()) {
            return Collections.emptyList();
        }

        try {
            return objectMapper.readValue(answersJson, new TypeReference<List<Integer>>() {});
        } catch (JsonProcessingException ex) {
            return Collections.emptyList();
        }
    }
}

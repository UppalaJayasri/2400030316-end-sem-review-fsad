package com.indianheritage.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_attempts", uniqueConstraints = @UniqueConstraint(columnNames = {"quiz_id", "user_name"}))
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private int totalQuestions;

    @Column(name = "answers_json", length = 500)
    private String answersJson;

    @Column(nullable = false)
    private LocalDateTime completedAt;

    public QuizAttempt() {
    }

    public QuizAttempt(Quiz quiz, String userName, int score, int totalQuestions) {
        this.quiz = quiz;
        this.userName = userName;
        this.score = score;
        this.totalQuestions = totalQuestions;
    }

    public QuizAttempt(Quiz quiz, String userName, int score, int totalQuestions, String answersJson) {
        this.quiz = quiz;
        this.userName = userName;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.answersJson = answersJson;
    }

    @PrePersist
    public void onCreate() {
        this.completedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public String getUserName() {
        return userName;
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public String getAnswersJson() {
        return answersJson;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
}

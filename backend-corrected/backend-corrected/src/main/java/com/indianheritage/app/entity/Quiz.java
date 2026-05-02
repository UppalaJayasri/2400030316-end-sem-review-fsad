package com.indianheritage.app.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quizzes")
public class Quiz {

    @Id
    @Column(length = 8)
    private String id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false)
    private boolean visible;

    @Column(nullable = false)
    private Integer questionCount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "starts_at")
    private LocalDateTime startsAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizQuestion> questions = new ArrayList<>();

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizAttempt> attempts = new ArrayList<>();

    public Quiz() {
    }

    public Quiz(String title) {
        this.title = title;
        this.visible = false;
    }

    @PrePersist
    public void onCreate() {
        if (this.id == null) {
            this.id = generateId();
        }
        this.createdAt = LocalDateTime.now();
        if (this.startsAt == null) {
            this.startsAt = this.createdAt;
        }
        if (this.expiresAt == null) {
            this.expiresAt = this.startsAt.plusDays(7);
        }
    }

    private String generateId() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        java.security.SecureRandom rnd = new java.security.SecureRandom();
        while (sb.length() < 8) {
            int index = rnd.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getStartsAt() {
        return startsAt != null ? startsAt : createdAt;
    }

    public void setStartsAt(LocalDateTime startsAt) {
        this.startsAt = startsAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public List<QuizQuestion> getQuestions() {
        return questions;
    }

    public List<QuizAttempt> getAttempts() {
        return attempts;
    }

    public boolean hasStarted() {
        LocalDateTime start = getStartsAt();
        return start != null && !LocalDateTime.now().isBefore(start);
    }

    public boolean isExpired() {
        return this.expiresAt != null && LocalDateTime.now().isAfter(this.expiresAt);
    }

    public boolean isActiveForUsers() {
        return this.visible && hasStarted() && !isExpired();
    }
}

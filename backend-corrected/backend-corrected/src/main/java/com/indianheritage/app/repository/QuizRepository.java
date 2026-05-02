package com.indianheritage.app.repository;

import com.indianheritage.app.entity.Quiz;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, String> {

    List<Quiz> findByVisibleTrueAndExpiresAtAfterOrderByCreatedAtDesc(LocalDateTime expiresAfter);

    @org.springframework.data.jpa.repository.Query("SELECT q FROM Quiz q WHERE q.visible = true AND q.startsAt <= :now AND q.expiresAt > :now ORDER BY q.createdAt DESC")
    List<Quiz> findActiveQuizzesForUser(@org.springframework.data.repository.query.Param("now") LocalDateTime now);

    List<Quiz> findAllByOrderByCreatedAtDesc();
}

package com.indianheritage.app.repository;

import com.indianheritage.app.entity.QuizQuestion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {

    List<QuizQuestion> findByQuiz_IdOrderByIdAsc(String quizId);

    long countByQuiz_Id(String quizId);

    void deleteByQuiz_Id(String quizId);
}

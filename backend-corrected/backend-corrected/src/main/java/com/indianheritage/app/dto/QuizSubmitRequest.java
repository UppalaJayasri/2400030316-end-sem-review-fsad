package com.indianheritage.app.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public class QuizSubmitRequest {

    @NotNull(message = "Quiz ID is required")
    private String quizId;

    @NotNull(message = "Answers are required")
    private List<Integer> answers;

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public List<Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Integer> answers) {
        this.answers = answers;
    }
}

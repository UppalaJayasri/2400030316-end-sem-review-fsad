package com.indianheritage.app.dto;

import java.util.List;

public record QuizResultResponse(
    int score,
    int totalQuestions,
    List<AnswerDetail> details
) {
    public record AnswerDetail(
        String questionText,
        String optionA,
        String optionB,
        String optionC,
        String optionD,
        int userAnswer,
        int correctOption,
        boolean correct,
        String explanation
    ) {
    }
}

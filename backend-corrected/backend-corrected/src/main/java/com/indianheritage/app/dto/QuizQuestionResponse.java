package com.indianheritage.app.dto;

public record QuizQuestionResponse(
    Long id,
    String questionText,
    String optionA,
    String optionB,
    String optionC,
    String optionD
) {
}

package com.indianheritage.app.dto;

public record AdminQuizQuestionResponse(
    Long id,
    String questionText,
    String optionA,
    String optionB,
    String optionC,
    String optionD,
    int correctOption,
    String explanation
) {
}

package com.indianheritage.app.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public class QuizCreateRequest {

    @NotBlank(message = "Quiz title is required")
    private String title;

    @NotNull(message = "Question count is required")
    @Min(value = 5, message = "Minimum 5 questions are required")
    @Max(value = 20, message = "Maximum 20 questions are allowed")
    private Integer questionCount;

    @NotNull(message = "Start date is required")
    private LocalDateTime startsAt;

    @NotNull(message = "End date is required")
    private LocalDateTime endsAt;

    @NotNull(message = "Visibility is required")
    private Boolean visible = Boolean.TRUE;

    @NotNull(message = "Questions are required")
    @Size(min = 5, max = 20, message = "Quiz must have between 5 and 20 questions")
    @Valid
    private List<QuestionPayload> questions;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(LocalDateTime startsAt) {
        this.startsAt = startsAt;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(LocalDateTime endsAt) {
        this.endsAt = endsAt;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public List<QuestionPayload> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionPayload> questions) {
        this.questions = questions;
    }

    public static class QuestionPayload {

        @NotBlank(message = "Question text is required")
        private String questionText;

        @NotBlank(message = "Option A is required")
        private String optionA;

        @NotBlank(message = "Option B is required")
        private String optionB;

        @NotBlank(message = "Option C is required")
        private String optionC;

        @NotBlank(message = "Option D is required")
        private String optionD;

        @NotNull(message = "Correct option is required")
        private Integer correctOption;

        private String explanation;

        public String getQuestionText() {
            return questionText;
        }

        public void setQuestionText(String questionText) {
            this.questionText = questionText;
        }

        public String getOptionA() {
            return optionA;
        }

        public void setOptionA(String optionA) {
            this.optionA = optionA;
        }

        public String getOptionB() {
            return optionB;
        }

        public void setOptionB(String optionB) {
            this.optionB = optionB;
        }

        public String getOptionC() {
            return optionC;
        }

        public void setOptionC(String optionC) {
            this.optionC = optionC;
        }

        public String getOptionD() {
            return optionD;
        }

        public void setOptionD(String optionD) {
            this.optionD = optionD;
        }

        public Integer getCorrectOption() {
            return correctOption;
        }

        public void setCorrectOption(Integer correctOption) {
            this.correctOption = correctOption;
        }

        public String getExplanation() {
            return explanation;
        }

        public void setExplanation(String explanation) {
            this.explanation = explanation;
        }
    }
}

package com.yib.your_ielts_book.dto;

import com.yib.your_ielts_book.model.QuestionCategory;
import com.yib.your_ielts_book.model.TestStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WritingTestUserDTO {
    private int id;

    @NotNull(message = "Customer id cannot be null")
    private int customerId;

    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
    private TestStatus status;
    private int duration;
    private QuestionCategory questionCategory;

    @NotBlank(message = "Answer cannot be blank")
    private String answer;
    private double score;
    private String feedback;

    // Replace entity with simple DTO
    private WritingQuestionSummary writingQuestion;

    // Nested DTO for WritingQuestion
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WritingQuestionSummary {
        private int id;
        private String question;
        private String imageUrl;
        // Add other fields you need
    }
}
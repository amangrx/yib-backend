package com.yib.your_ielts_book.dto;

import com.yib.your_ielts_book.model.QuestionCategory;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WritingTestUserDTO {
    private int customerId;

    @PositiveOrZero(message = "Duration must be positive or zero")
    private int duration;

    private QuestionCategory questionCategory;

    @NotNull(message = "Question id cannot be null")
    @Positive(message = "Question id must be positive")
    private int questionId;

    @NotBlank(message = "Answer cannot be blank")
    private String answer;

    private double score;

    private String feedback;
}
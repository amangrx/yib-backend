package com.yib.your_ielts_book.dto;

import com.yib.your_ielts_book.model.QuestionCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadingAndListeningTestUserDTO {
    private int customerId;

    @PositiveOrZero(message = "Duration must be positive or zero")
    private int duration;

    private QuestionCategory questionCategory;

    @NotNull(message = "Question id cannot be null")
    @Positive(message = "Question id must be positive")
    private int questionId;

    private List<String> answer;

    private double score;

    private String feedback;
}



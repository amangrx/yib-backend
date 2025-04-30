package com.yib.your_ielts_book.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewWritingRequestDTO {
    @NotNull(message = "Score cannot be null")
    @Min(value = 0, message = "Score must be at least 0")
    @Max(value = 9, message = "Score must be at most 9")
    private double score;

    @NotBlank(message = "Feedback cannot be blank")
    private String feedback;
}

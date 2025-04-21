package com.yib.your_ielts_book.dto;
import com.yib.your_ielts_book.model.QuestionCategory;
import com.yib.your_ielts_book.model.QuestionDifficulty;
import com.yib.your_ielts_book.model.WritingTaskType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WritingQuestionDTO {
    private Integer id;

    @NotNull(message = "Category is required")
    private QuestionCategory category;

    @NotNull(message = "Difficulty is required")
    private QuestionDifficulty difficulty;

    private String createdBy;
    private LocalDateTime createdAt;

    // Fields specific to WritingQuestion
    @NotNull(message = "Writing task type is required")
    private WritingTaskType writingTaskType;

    @NotBlank(message = "Question text is required")
    private String question;

    private String answer;
    private String imageUrl;

    // Transient field for file upload (not persisted)
    private transient MultipartFile imageFile;

}

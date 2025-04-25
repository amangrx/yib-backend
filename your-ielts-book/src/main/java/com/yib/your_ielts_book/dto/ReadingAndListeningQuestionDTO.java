package com.yib.your_ielts_book.dto;

import com.yib.your_ielts_book.model.QuestionCategory;
import com.yib.your_ielts_book.model.QuestionDifficulty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadingAndListeningQuestionDTO {
    private Integer id;

    @NotNull(message = "Category is required")
    private QuestionCategory category;

    @NotNull(message = "Difficulty is required")
    private QuestionDifficulty difficulty;

    private Integer expertId;
    private String createdBy;
    private LocalDateTime createdAt;

    @NotNull(message = "Title is required")
    private String title;

    // For file paths (stored in database)
    private String pdfFilePath;
    private String listeningAudioUrl;

    // For frontend access (generated URLs)
    private String pdfFileUrl;
    private String audioFileUrl;

    @NotNull(message = "Answers are required")
    private List<String> answers;

    // Transient fields for file upload (not persisted)
    private transient MultipartFile pdfFile;
    private transient MultipartFile audioFile;
}
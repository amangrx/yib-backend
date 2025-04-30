package com.yib.your_ielts_book.dto;

import com.yib.your_ielts_book.model.QuestionCategory;
import com.yib.your_ielts_book.model.TestStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WritingTestAnswerResponseDTO {
    private int id;
    private int customerId;
    private LocalDateTime submittedAt;
    private TestStatus status;
    private int duration;
    private QuestionCategory questionCategory;
    private String answer;
    private double score;
    private String feedback;
    private WritingQuestionDTO writingQuestion;
}
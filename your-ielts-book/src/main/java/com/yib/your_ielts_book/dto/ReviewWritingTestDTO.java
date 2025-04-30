package com.yib.your_ielts_book.dto;

import com.yib.your_ielts_book.model.QuestionCategory;
import com.yib.your_ielts_book.model.TestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewWritingTestDTO {
    private int testId;               // Test ID
    private int customerId;           // User who took the test
    private String customerName;      // User's name (will need to fetch this)
    private LocalDateTime submittedAt; // When the test was submitted
    private TestStatus status;        // Current status of the test
    private int questionId;           // Writing question ID
    private QuestionCategory questionCategory; // Question category
    private int duration;             // Time taken (seconds)
}
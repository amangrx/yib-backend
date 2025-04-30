package com.yib.your_ielts_book.projection;

import com.yib.your_ielts_book.model.QuestionCategory;
import com.yib.your_ielts_book.model.TestStatus;

import java.time.LocalDateTime;

public interface ReviewTestProjection {
    int getTestId();
    int getCustomerId();
    String getCustomerName();
    LocalDateTime getSubmittedAt();
    TestStatus getStatus();
    int getQuestionId();
    QuestionCategory getQuestionCategory();
    int getDuration();
}

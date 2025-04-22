package com.yib.your_ielts_book.mapper;

import com.yib.your_ielts_book.dto.WritingTestUserDTO;
import com.yib.your_ielts_book.model.WritingTestUser;

public class WritingTestUserMapper {
    public static WritingTestUserDTO toDto(WritingTestUser test) {
        WritingTestUserDTO.WritingQuestionSummary questionSummary = null;
        if (test.getWritingQuestion() != null) {
            questionSummary = new WritingTestUserDTO.WritingQuestionSummary(
                    test.getWritingQuestion().getId(),
                    test.getWritingQuestion().getQuestion(),
                    test.getWritingQuestion().getImageUrl()
            );
        }

        return new WritingTestUserDTO(
                test.getId(),
                test.getCustomerId(),
                test.getStartedAt(),
                test.getSubmittedAt(),
                test.getStatus(),
                test.getDuration(),
                test.getQuestionCategory(),
                test.getAnswer(),
                test.getScore(),
                test.getFeedback(),
                questionSummary
        );
    }

    public static WritingTestUser toEntity(WritingTestUserDTO dto) {
        WritingTestUser test = new WritingTestUser();
        test.setId(dto.getId());
        test.setCustomerId(dto.getCustomerId());
        test.setStartedAt(dto.getStartedAt());
        test.setSubmittedAt(dto.getSubmittedAt());
        test.setStatus(dto.getStatus());
        test.setDuration(dto.getDuration());
        test.setQuestionCategory(dto.getQuestionCategory());
        test.setAnswer(dto.getAnswer());
        test.setScore(dto.getScore());
        test.setFeedback(dto.getFeedback());
        return test;
    }
}
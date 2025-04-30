package com.yib.your_ielts_book.mapper;

import com.yib.your_ielts_book.dto.WritingQuestionDTO;
import com.yib.your_ielts_book.dto.WritingTestAnswerResponseDTO;
import com.yib.your_ielts_book.model.WritingQuestion;
import com.yib.your_ielts_book.model.WritingTestUser;
import org.springframework.stereotype.Component;

@Component
public class WritingTestAnsMapper {
    public WritingTestAnswerResponseDTO mapToResponseDTO(WritingTestUser writingTestUser) {
        WritingTestAnswerResponseDTO response = new WritingTestAnswerResponseDTO();
        response.setId(writingTestUser.getId());
        response.setCustomerId(writingTestUser.getCustomerId());
        response.setSubmittedAt(writingTestUser.getSubmittedAt());
        response.setStatus(writingTestUser.getStatus());
        response.setDuration(writingTestUser.getDuration());
        response.setQuestionCategory(writingTestUser.getQuestionCategory());
        response.setAnswer(writingTestUser.getAnswer());
        response.setScore(writingTestUser.getScore());
        response.setFeedback(writingTestUser.getFeedback());

        // Map the associated writing question
        if (writingTestUser.getWritingQuestion() != null) {
            response.setWritingQuestion(mapToWritingQuestionDTO(writingTestUser.getWritingQuestion()));
        }

        return response;
    }

    public WritingQuestionDTO mapToWritingQuestionDTO(WritingQuestion question) {
        WritingQuestionDTO dto = new WritingQuestionDTO();
        return dto;
    }
}

package com.yib.your_ielts_book.mapper;

import com.yib.your_ielts_book.dto.ReadingAndListeningTestUserDTO;
import com.yib.your_ielts_book.model.ReadingAndListeningTestUser;
import com.yib.your_ielts_book.model.TestStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReadingAndListeningTestUserMapper {

    public ReadingAndListeningTestUser toEntity(ReadingAndListeningTestUserDTO dto) {
        if (dto == null) {
            return null;
        }

        ReadingAndListeningTestUser entity = new ReadingAndListeningTestUser();

        // Map fields from Test superclass
        entity.setQuestionId(dto.getQuestionId());
        entity.setQuestionCategory(dto.getQuestionCategory());
        entity.setCustomerId(dto.getCustomerId());
        entity.setDuration(dto.getDuration());
        entity.setSubmittedAt(LocalDateTime.now());
        entity.setStatus(TestStatus.SUBMITTED);

        // Map fields from ReadingAndListeningTestUser
        entity.setAnswers(dto.getAnswer());
        entity.setScore(dto.getScore());
        entity.setFeedback(dto.getFeedback());

        // Note: The 'question' relationship is not set here as it requires repository access
        return entity;
    }

    public ReadingAndListeningTestUserDTO toDto(ReadingAndListeningTestUser entity) {
        if (entity == null) {
            return null;
        }

        ReadingAndListeningTestUserDTO dto = new ReadingAndListeningTestUserDTO();

        // Map fields from Test superclass
        dto.setQuestionId(entity.getQuestionId());
        dto.setQuestionCategory(entity.getQuestionCategory());
        dto.setCustomerId(entity.getCustomerId());
        dto.setDuration(entity.getDuration());

        // Map fields from ReadingAndListeningTestUser
        dto.setAnswer(entity.getAnswers());
        dto.setScore(entity.getScore());
        dto.setFeedback(entity.getFeedback());

        return dto;
    }
}
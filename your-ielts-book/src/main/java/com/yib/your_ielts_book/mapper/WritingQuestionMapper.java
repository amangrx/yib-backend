package com.yib.your_ielts_book.mapper;

import com.yib.your_ielts_book.dto.WritingQuestionDTO;
import com.yib.your_ielts_book.model.WritingQuestion;

import java.time.LocalDateTime;

public class WritingQuestionMapper {
    public static WritingQuestion toEntity(WritingQuestionDTO dto) {
        WritingQuestion writingQuestion = new WritingQuestion();
        writingQuestion.setCategory(dto.getCategory());
        writingQuestion.setDifficulty(dto.getDifficulty());
        writingQuestion.setExpertId(dto.getExpertId());
        writingQuestion.setCreatedBy(dto.getCreatedBy());
        writingQuestion.setCreatedAt(LocalDateTime.now());
        writingQuestion.setWritingTaskType(dto.getWritingTaskType());
        writingQuestion.setQuestion(dto.getQuestion());
        writingQuestion.setAnswer(dto.getAnswer());
        writingQuestion.setImageUrl(dto.getImageUrl());
        return writingQuestion;
    }

    public static WritingQuestionDTO toDTO(WritingQuestion entity) {
        WritingQuestionDTO dto = new WritingQuestionDTO();
        dto.setId(entity.getId());
        dto.setCategory(entity.getCategory());
        dto.setDifficulty(entity.getDifficulty());
        dto.setExpertId(entity.getExpertId());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setWritingTaskType(entity.getWritingTaskType());
        dto.setQuestion(entity.getQuestion());
        dto.setAnswer(entity.getAnswer());
        dto.setImageUrl(entity.getImageUrl());
        return dto;
    }
}

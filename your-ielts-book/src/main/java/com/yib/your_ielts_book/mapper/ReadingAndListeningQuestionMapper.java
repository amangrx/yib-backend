package com.yib.your_ielts_book.mapper;

import com.yib.your_ielts_book.dto.ReadingAndListeningQuestionDTO;
import com.yib.your_ielts_book.model.ReadingAndListeningQuestion;

public class ReadingAndListeningQuestionMapper {
    public static ReadingAndListeningQuestion toEntity(ReadingAndListeningQuestionDTO dto) {
        ReadingAndListeningQuestion question = new ReadingAndListeningQuestion();
        question.setCategory(dto.getCategory());
        question.setDifficulty(dto.getDifficulty());
        question.setExpertId(dto.getExpertId());
        question.setCreatedBy(dto.getCreatedBy());
        question.setCreatedAt(dto.getCreatedAt());
        question.setTitle(dto.getTitle());
        question.setPdfFilePath(dto.getPdfFilePath());
        question.setListeningAudioUrl(dto.getListeningAudioUrl());
        question.setAnswers(dto.getAnswers());
        return question;
    }

    public static ReadingAndListeningQuestionDTO toDTO(ReadingAndListeningQuestion entity) {
        ReadingAndListeningQuestionDTO dto = new ReadingAndListeningQuestionDTO();
        dto.setId(entity.getId());
        dto.setCategory(entity.getCategory());
        dto.setDifficulty(entity.getDifficulty());
        dto.setExpertId(entity.getExpertId());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setTitle(entity.getTitle());
        dto.setPdfFilePath(entity.getPdfFilePath());
        dto.setListeningAudioUrl(entity.getListeningAudioUrl());
        dto.setAnswers(entity.getAnswers());
        return dto;
    }
}
package com.yib.your_ielts_book.service;

import com.yib.your_ielts_book.dto.WritingQuestionDTO;

import java.util.List;

public interface WritingQuestionService {
    List<WritingQuestionDTO> getAllWritingQuestions();

    WritingQuestionDTO getWritingQuestionById(int questionId);
}

package com.yib.your_ielts_book.service;

import com.yib.your_ielts_book.dto.ReadingAndListeningQuestionDTO;

import java.util.List;

public interface ReadingAndListeningQuestionService {
    List<ReadingAndListeningQuestionDTO> getAllReadingAndListeningQuestions();

    ReadingAndListeningQuestionDTO getReadingAndListeningQuestionsById(int questionId);
}

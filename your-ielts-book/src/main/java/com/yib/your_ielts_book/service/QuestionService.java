package com.yib.your_ielts_book.service;

import com.yib.your_ielts_book.dto.WritingQuestionDTO;
import com.yib.your_ielts_book.model.WritingQuestion;

public interface QuestionService {
    WritingQuestionDTO createWritingQues(WritingQuestionDTO dto, String jwt);
}
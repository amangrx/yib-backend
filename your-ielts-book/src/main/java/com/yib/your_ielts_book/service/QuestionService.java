package com.yib.your_ielts_book.service;

import com.yib.your_ielts_book.dto.ReadingAndListeningQuestionDTO;
import com.yib.your_ielts_book.dto.WritingQuestionDTO;
import org.springframework.web.multipart.MultipartFile;

public interface QuestionService {
    WritingQuestionDTO createWritingQues(WritingQuestionDTO dto, String jwt);

    ReadingAndListeningQuestionDTO creatingReadingListeningQues(ReadingAndListeningQuestionDTO dto, MultipartFile pdfFile, MultipartFile audioFile, String jwt);
}
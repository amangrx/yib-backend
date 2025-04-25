package com.yib.your_ielts_book.service;

import com.yib.your_ielts_book.dto.WritingTestUserDTO;
import jakarta.validation.Valid;

public interface WritingTestUserService {

    WritingTestUserDTO submitWritingTest(String jwt, @Valid WritingTestUserDTO userAnswerDTO);
}

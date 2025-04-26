package com.yib.your_ielts_book.service;

import com.yib.your_ielts_book.dto.ReadingAndListeningTestUserDTO;
import jakarta.validation.Valid;

public interface ReadingAndListeningTestUserService {
    ReadingAndListeningTestUserDTO submitReadingOrListeningTest(String jwt, @Valid ReadingAndListeningTestUserDTO userAnswerDTO);
}

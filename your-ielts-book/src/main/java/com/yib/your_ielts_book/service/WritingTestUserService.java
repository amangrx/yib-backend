package com.yib.your_ielts_book.service;

import com.yib.your_ielts_book.dto.*;
import jakarta.validation.Valid;

import java.util.List;

public interface WritingTestUserService {

    WritingTestUserDTO submitWritingTest(String jwt, @Valid WritingTestUserDTO userAnswerDTO);

    List<ReviewWritingTestDTO> getTestsForExpertReview(String jwt);

    WritingTestAnswerResponseDTO getWritingTestAnswer(int testId);

    ReviewResponseDTO submitTestReview(int testId, @Valid ReviewWritingRequestDTO reviewDTO, String jwt);
}

package com.yib.your_ielts_book.service.impls;

import com.yib.your_ielts_book.config.JWTService;
import com.yib.your_ielts_book.dto.WritingTestUserDTO;
import com.yib.your_ielts_book.mapper.WritingTestUserMapper;
import com.yib.your_ielts_book.model.TestStatus;
import com.yib.your_ielts_book.model.WritingQuestion;
import com.yib.your_ielts_book.model.WritingTestUser;
import com.yib.your_ielts_book.repo.WritingQuestionRepo;
import com.yib.your_ielts_book.repo.WritingTestUserRepo;
import com.yib.your_ielts_book.service.WritingTestUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WritingTestUserServiceImpl implements WritingTestUserService {
    private final WritingTestUserRepo repo;
    private final WritingQuestionRepo questionRepo;
    private final JWTService jwtService;

    public WritingTestUserServiceImpl(WritingTestUserRepo repo, WritingQuestionRepo questionRepo, JWTService jwtService) {
        this.repo = repo;
        this.questionRepo = questionRepo;
        this.jwtService = jwtService;
    }

    @Override
    public WritingTestUserDTO startWritingTest(int questionId, String jwt) {
        try {
            if (jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);
            }
            int customerId = jwtService.extractUserId(jwt);

            WritingQuestion question = questionRepo.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            WritingTestUser newTest = new WritingTestUser();
            newTest.setCustomerId(customerId);
            newTest.setStartedAt(LocalDateTime.now());
            newTest.setStatus(TestStatus.IN_PROGRESS);
            newTest.setQuestionCategory(question.getCategory());
            newTest.setWritingQuestion(question);

            WritingTestUser savedTest = repo.save(newTest);
            return WritingTestUserMapper.toDto(savedTest);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public WritingTestUserDTO submitWritingTest(String jwt, WritingTestUserDTO userAnswerDTO) {
        try{
            if (jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);
            }
            int customerId = jwtService.extractUserId(jwt);

            WritingTestUser submitTest = repo.findByIdAndCustomerId(userAnswerDTO.getId(), customerId)
                    .orElseThrow(() -> new RuntimeException("Test not found"));

            submitTest.setSubmittedAt(LocalDateTime.now());
            submitTest.setStatus(TestStatus.SUBMITTED);
            submitTest.setDuration(userAnswerDTO.getDuration());
            submitTest.setAnswer(userAnswerDTO.getAnswer());

            WritingTestUser savedTest = repo.save(submitTest);
            return WritingTestUserMapper.toDto(savedTest);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}

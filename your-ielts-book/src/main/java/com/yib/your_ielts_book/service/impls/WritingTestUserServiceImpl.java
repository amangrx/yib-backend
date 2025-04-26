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
    private final WritingTestUserMapper mapper;

    public WritingTestUserServiceImpl(WritingTestUserRepo repo, WritingQuestionRepo questionRepo, JWTService jwtService, WritingTestUserMapper mapper) {
        this.repo = repo;
        this.questionRepo = questionRepo;
        this.jwtService = jwtService;
        this.mapper = mapper;
    }

    @Override
    public WritingTestUserDTO submitWritingTest(String jwt, WritingTestUserDTO userAnswerDTO) {
        try{
            if (jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);
            }
            int customerId = jwtService.extractUserId(jwt);

            WritingQuestion question = questionRepo.findById(userAnswerDTO.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            WritingTestUser newTest = new WritingTestUser();

            newTest.setCustomerId(customerId);
            newTest.setSubmittedAt(LocalDateTime.now());
            newTest.setStatus(TestStatus.SUBMITTED);
            newTest.setDuration(userAnswerDTO.getDuration());
            newTest.setQuestionCategory(question.getCategory());
            newTest.setQuestionId(userAnswerDTO.getQuestionId());
            newTest.setWritingQuestion(question);
            newTest.setAnswer(userAnswerDTO.getAnswer());

            WritingTestUser savedTest = repo.save(newTest);
            return mapper.toDTO(savedTest);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}

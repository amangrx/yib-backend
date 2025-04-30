package com.yib.your_ielts_book.service.impls;

import com.yib.your_ielts_book.config.JWTService;
import com.yib.your_ielts_book.dto.*;
import com.yib.your_ielts_book.exception.ResourceNotFoundException;
import com.yib.your_ielts_book.mapper.WritingTestAnsMapper;
import com.yib.your_ielts_book.mapper.WritingTestUserMapper;
import com.yib.your_ielts_book.model.TestStatus;
import com.yib.your_ielts_book.model.WritingQuestion;
import com.yib.your_ielts_book.model.WritingTestUser;
import com.yib.your_ielts_book.projection.ReviewTestProjection;
import com.yib.your_ielts_book.repo.WritingQuestionRepo;
import com.yib.your_ielts_book.repo.WritingTestUserRepo;
import com.yib.your_ielts_book.service.WritingTestUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WritingTestUserServiceImpl implements WritingTestUserService {
    private final WritingTestUserRepo repo;
    private final WritingQuestionRepo questionRepo;
    private final JWTService jwtService;
    private final WritingTestUserMapper mapper;
    private final WritingTestAnsMapper ansMapper;

    public WritingTestUserServiceImpl(WritingTestUserRepo repo, WritingQuestionRepo questionRepo, JWTService jwtService, WritingTestUserMapper mapper, WritingTestAnsMapper ansMapper) {
        this.repo = repo;
        this.questionRepo = questionRepo;
        this.jwtService = jwtService;
        this.mapper = mapper;
        this.ansMapper = ansMapper;
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

    @Override
    public List<ReviewWritingTestDTO> getTestsForExpertReview(String jwt) {
        try {
            // Extract expert ID from JWT
            if (jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);
            }
            int expertId = jwtService.extractUserId(jwt);

            // Fetch projected data
            List<ReviewTestProjection> projections = repo.findTestsForExpertReview(expertId);

            // Convert to DTO
            return projections.stream()
                    .map(p -> new ReviewWritingTestDTO(
                            p.getTestId(),
                            p.getCustomerId(),
                            p.getCustomerName(),
                            p.getSubmittedAt(),
                            p.getStatus(),
                            p.getQuestionId(),
                            p.getQuestionCategory(),
                            p.getDuration()
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get tests for expert review", e);
        }
    }

    @Override
    public WritingTestAnswerResponseDTO getWritingTestAnswer(int testId) {
        WritingTestUser writingTestUser = repo.findById(testId)
                .orElseThrow(() -> new ResourceNotFoundException("Test not found"));
        return ansMapper.mapToResponseDTO(writingTestUser);
    }

    @Override
    public ReviewResponseDTO submitTestReview(int testId, ReviewWritingRequestDTO reviewDTO, String jwt) {
        WritingTestUser test = repo.findById(testId)
                .orElseThrow(() -> new ResourceNotFoundException("Test not found with id: " + testId));

        test.setScore(reviewDTO.getScore());
        test.setFeedback(reviewDTO.getFeedback());
        test.setStatus(TestStatus.EVALUATED);

        repo.save(test);

        ReviewResponseDTO response = new ReviewResponseDTO();
        response.setTestId(testId);
        response.setStatus("COMPLETED");
        response.setMessage("Review submitted successfully");
        return response;
    }
}

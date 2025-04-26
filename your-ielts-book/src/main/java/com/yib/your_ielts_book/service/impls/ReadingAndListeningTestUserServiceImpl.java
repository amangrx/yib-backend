package com.yib.your_ielts_book.service.impls;

import com.yib.your_ielts_book.config.JWTService;
import com.yib.your_ielts_book.dto.ReadingAndListeningQuestionDTO;
import com.yib.your_ielts_book.dto.ReadingAndListeningTestUserDTO;
import com.yib.your_ielts_book.exception.ResourceNotFoundException;
import com.yib.your_ielts_book.mapper.ReadingAndListeningTestUserMapper;
import com.yib.your_ielts_book.model.ReadingAndListeningQuestion;
import com.yib.your_ielts_book.model.ReadingAndListeningTestUser;
import com.yib.your_ielts_book.model.TestStatus;
import com.yib.your_ielts_book.repo.ReadingAndListeningQuestionRepo;
import com.yib.your_ielts_book.repo.ReadingAndListeningTestUserRepo;
import com.yib.your_ielts_book.service.ReadingAndListeningTestUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReadingAndListeningTestUserServiceImpl implements ReadingAndListeningTestUserService {

    private final ReadingAndListeningQuestionRepo readingAndListeningQuestionRepo;
    private final ReadingAndListeningTestUserRepo readingAndListeningTestUserRepo;
    private final ReadingAndListeningTestUserMapper readingAndListeningTestUserMapper;
    private final JWTService jwtService;

    public ReadingAndListeningTestUserServiceImpl(ReadingAndListeningQuestionRepo readingAndListeningQuestionRepo, ReadingAndListeningTestUserRepo readingAndListeningTestUserRepo, ReadingAndListeningTestUserMapper readingAndListeningTestUserMapper, JWTService jwtService) {
        this.readingAndListeningQuestionRepo = readingAndListeningQuestionRepo;
        this.readingAndListeningTestUserRepo = readingAndListeningTestUserRepo;
        this.readingAndListeningTestUserMapper = readingAndListeningTestUserMapper;
        this.jwtService = jwtService;
    }

    @Override
    public ReadingAndListeningTestUserDTO submitReadingOrListeningTest(String jwt, ReadingAndListeningTestUserDTO userAnswerDTO) {
        try{
            if(jwt.startsWith("Bearer")){
                jwt = jwt.substring(7);
            }
            int customerId = jwtService.extractUserId(jwt);
            System.out.println("service ma " + userAnswerDTO.getQuestionId());
            ReadingAndListeningQuestion question = readingAndListeningQuestionRepo.findById(userAnswerDTO.getQuestionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + userAnswerDTO.getQuestionId()));
            System.out.println("second service ma " + userAnswerDTO.getQuestionId());

            ReadingAndListeningTestUser newTest = readingAndListeningTestUserMapper.toEntity(userAnswerDTO);

            newTest.setCustomerId(customerId);
            newTest.setSubmittedAt(LocalDateTime.now());
            newTest.setStatus(TestStatus.SUBMITTED);
            newTest.setQuestion(question);

            // Set category from question if not provided in DTO
            if (userAnswerDTO.getQuestionCategory() == null) {
                newTest.setQuestionCategory(question.getCategory());
            }

            // Save and return mapped DTO
            ReadingAndListeningTestUser savedTest = readingAndListeningTestUserRepo.save(newTest);
            return readingAndListeningTestUserMapper.toDto(savedTest);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}

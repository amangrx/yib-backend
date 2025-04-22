package com.yib.your_ielts_book.service.impls;

import com.yib.your_ielts_book.config.JWTService;
import com.yib.your_ielts_book.dto.WritingQuestionDTO;
import com.yib.your_ielts_book.mapper.WritingQuestionMapper;
import com.yib.your_ielts_book.model.QuestionCategory;
import com.yib.your_ielts_book.model.WritingQuestion;
import com.yib.your_ielts_book.repo.WritingQuestionRepo;
import com.yib.your_ielts_book.service.CloudinaryService;
import com.yib.your_ielts_book.service.QuestionService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final WritingQuestionRepo writingQuestionRepo;
    private final CloudinaryService cloudinaryService;
    private final JWTService jwtService;

    public QuestionServiceImpl(WritingQuestionRepo writingQuestionRepo, CloudinaryService cloudinaryService, JWTService jwtService) {
        this.writingQuestionRepo = writingQuestionRepo;
        this.cloudinaryService = cloudinaryService;
        this.jwtService = jwtService;
    }

    @Override
    public WritingQuestionDTO createWritingQues(WritingQuestionDTO dto, String jwt) {
        try{
            if (jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);
            }
            int expertId = jwtService.extractUserId(jwt);
            String name = jwtService.extractFullName(jwt);
            if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
                try {
                    dto.setImageUrl(cloudinaryService.uploadWritingImage(dto.getImageFile()));
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload image", e);
                }
            }
            WritingQuestion writingQuestion = WritingQuestionMapper.toEntity(dto);
            writingQuestion.setCategory(QuestionCategory.WRITING);
            writingQuestion.setExpertId(expertId);
            writingQuestion.setCreatedBy(name);
            writingQuestion.setCreatedAt(LocalDateTime.now());
            WritingQuestion savedQuestion = writingQuestionRepo.save(writingQuestion);
            return WritingQuestionMapper.toDTO(savedQuestion);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}

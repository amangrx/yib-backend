package com.yib.your_ielts_book.service.impls;

import com.yib.your_ielts_book.config.JWTService;
import com.yib.your_ielts_book.dto.ReadingAndListeningQuestionDTO;
import com.yib.your_ielts_book.dto.WritingQuestionDTO;
import com.yib.your_ielts_book.mapper.ReadingAndListeningQuestionMapper;
import com.yib.your_ielts_book.mapper.WritingQuestionMapper;
import com.yib.your_ielts_book.model.QuestionCategory;
import com.yib.your_ielts_book.model.ReadingAndListeningQuestion;
import com.yib.your_ielts_book.model.WritingQuestion;
import com.yib.your_ielts_book.repo.ReadingAndListeningQuestionRepo;
import com.yib.your_ielts_book.repo.WritingQuestionRepo;
import com.yib.your_ielts_book.service.CloudinaryService;
import com.yib.your_ielts_book.service.QuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final WritingQuestionRepo writingQuestionRepo;
    private final ReadingAndListeningQuestionRepo readingAndListeningQuestionRepo;
    private final CloudinaryService cloudinaryService;
    private final JWTService jwtService;

    public QuestionServiceImpl(WritingQuestionRepo writingQuestionRepo, ReadingAndListeningQuestionRepo readingAndListeningQuestionRepo, CloudinaryService cloudinaryService, JWTService jwtService) {
        this.writingQuestionRepo = writingQuestionRepo;
        this.readingAndListeningQuestionRepo = readingAndListeningQuestionRepo;
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

    @Override
    @Transactional
    public ReadingAndListeningQuestionDTO creatingReadingListeningQues(
            ReadingAndListeningQuestionDTO dto,
            MultipartFile pdfFile,
            MultipartFile audioFile,
            String jwt) {
        try {
            if (jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);
            }
            int expertId = jwtService.extractUserId(jwt);
            String expertName = jwtService.extractFullName(jwt);

            if (dto.getCategory() == null) {
                throw new IllegalArgumentException("Question category is required");
            }

            String pdfUrl = cloudinaryService.uploadPdf(pdfFile);
            dto.setPdfFilePath(pdfUrl);

            String audioUrl = null;
            if (dto.getCategory() == QuestionCategory.LISTENING) {
                if (audioFile == null || audioFile.isEmpty()) {
                    throw new IllegalArgumentException("Audio file is required for LISTENING questions");
                }
                audioUrl = cloudinaryService.uploadAudio(audioFile);
                dto.setListeningAudioUrl(audioUrl);
            } else {
                dto.setListeningAudioUrl(null);
            }

            dto.setExpertId(expertId);
            dto.setCreatedBy(expertName);
            dto.setCreatedAt(LocalDateTime.now());

            ReadingAndListeningQuestion question = ReadingAndListeningQuestionMapper.toEntity(dto);
            ReadingAndListeningQuestion savedQuestion = readingAndListeningQuestionRepo.save(question);
            return ReadingAndListeningQuestionMapper.toDTO(savedQuestion);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload files: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Validation error: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create question: " + e.getMessage(), e);
        }
    }
}

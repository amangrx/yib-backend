package com.yib.your_ielts_book.service.impls;

import com.yib.your_ielts_book.config.JWTService;
import com.yib.your_ielts_book.dto.ReadingAndListeningQuestionDTO;
import com.yib.your_ielts_book.exception.ResourceNotFoundException;
import com.yib.your_ielts_book.mapper.ReadingAndListeningQuestionMapper;
import com.yib.your_ielts_book.model.ReadingAndListeningQuestion;
import com.yib.your_ielts_book.repo.ReadingAndListeningQuestionRepo;
import com.yib.your_ielts_book.service.ReadingAndListeningQuestionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReadingAndListeningQuestionServiceImpl implements ReadingAndListeningQuestionService {

    private final String FILE_STORAGE_PATH = "src/main/resources/static/uploads/";
    private final String PDF_DIR = "pdfs/";
    private final String AUDIO_DIR = "audios/";

    private final ReadingAndListeningQuestionRepo readingAndListeningQuestionRepo;
    private final ReadingAndListeningQuestionMapper readingAndListeningQuestionMapper;
    private final JWTService jwtService;

    public ReadingAndListeningQuestionServiceImpl(ReadingAndListeningQuestionRepo readingAndListeningQuestionRepo, ReadingAndListeningQuestionMapper readingAndListeningQuestionMapper, JWTService jwtService) {
        this.readingAndListeningQuestionRepo = readingAndListeningQuestionRepo;
        this.readingAndListeningQuestionMapper = readingAndListeningQuestionMapper;
        this.jwtService = jwtService;
    }

    @Override
    public List<ReadingAndListeningQuestionDTO> getAllReadingAndListeningQuestions() {
        return readingAndListeningQuestionRepo.findAll()
                .stream()
                .map(readingAndListeningQuestionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReadingAndListeningQuestionDTO getReadingAndListeningQuestionsById(int questionId) {
        // Fetch the question from the database
        ReadingAndListeningQuestion question = readingAndListeningQuestionRepo.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + questionId));

        // Convert entity to DTO
        ReadingAndListeningQuestionDTO dto = readingAndListeningQuestionMapper.toDTO(question);

        // Generate URLs for file access
        if (question.getPdfFilePath() != null && !question.getPdfFilePath().isEmpty()) {
            String fileName = getFileNameFromPath(question.getPdfFilePath());
            dto.setPdfFileUrl("/api/files/" + PDF_DIR + fileName);
        }

        if (question.getListeningAudioUrl() != null && !question.getListeningAudioUrl().isEmpty()) {
            String fileName = getFileNameFromPath(question.getListeningAudioUrl());
            dto.setAudioFileUrl("/api/files/" + AUDIO_DIR + fileName);
        }

        return dto;
    }

    private String getFileNameFromPath(String path) {
        if (path == null) return null;
        int lastSlashIndex = path.lastIndexOf("/");
        return lastSlashIndex != -1 ? path.substring(lastSlashIndex + 1) : path;
    }
}

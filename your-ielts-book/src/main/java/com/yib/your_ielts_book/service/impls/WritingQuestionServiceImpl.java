package com.yib.your_ielts_book.service.impls;

import com.yib.your_ielts_book.dto.WritingQuestionDTO;
import com.yib.your_ielts_book.exception.ResourceNotFoundException;
import com.yib.your_ielts_book.mapper.WritingQuestionMapper;
import com.yib.your_ielts_book.model.WritingQuestion;
import com.yib.your_ielts_book.repo.WritingQuestionRepo;
import com.yib.your_ielts_book.service.WritingQuestionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WritingQuestionServiceImpl implements WritingQuestionService {

    private final WritingQuestionRepo writingQuestionRepo;

    public WritingQuestionServiceImpl(WritingQuestionRepo writingQuestionRepo) {
        this.writingQuestionRepo = writingQuestionRepo;
    }

    @Override
    public List<WritingQuestionDTO> getAllWritingQuestions() {
        return writingQuestionRepo.findAll()
                .stream()
                .map(WritingQuestionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public WritingQuestionDTO getWritingQuestionById(int questionId) {
        WritingQuestion detail = writingQuestionRepo.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
        return WritingQuestionMapper.toDTO(detail);
    }
}

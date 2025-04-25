package com.yib.your_ielts_book.mapper;

import com.yib.your_ielts_book.dto.ReadingAndListeningQuestionDTO;
import com.yib.your_ielts_book.model.ReadingAndListeningQuestion;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ReadingAndListeningQuestionMapper {

    private final ModelMapper modelMapper;

    public ReadingAndListeningQuestionMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ReadingAndListeningQuestionDTO toDTO(ReadingAndListeningQuestion readingAndListeningQuestion) {
        return modelMapper.map(readingAndListeningQuestion, ReadingAndListeningQuestionDTO.class);
    }

    public ReadingAndListeningQuestion toEntity(ReadingAndListeningQuestionDTO readingAndListeningQuestionDTO) {
        return modelMapper.map(readingAndListeningQuestionDTO, ReadingAndListeningQuestion.class);
    }
}
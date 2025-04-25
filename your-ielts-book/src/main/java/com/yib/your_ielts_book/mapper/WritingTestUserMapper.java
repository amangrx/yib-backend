package com.yib.your_ielts_book.mapper;

import com.yib.your_ielts_book.dto.WritingTestUserDTO;
import com.yib.your_ielts_book.model.WritingTestUser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class WritingTestUserMapper {
    private final ModelMapper modelMapper;

    public WritingTestUserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public WritingTestUserDTO toDTO(WritingTestUser writingTestUser) {
        return modelMapper.map(writingTestUser, WritingTestUserDTO.class);
    }

    public WritingTestUser toEntity(WritingTestUserDTO writingTestUserDTO) {
        return modelMapper.map(writingTestUserDTO, WritingTestUser.class);
    }
}
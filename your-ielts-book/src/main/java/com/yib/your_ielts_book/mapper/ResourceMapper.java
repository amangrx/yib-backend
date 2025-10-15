package com.yib.your_ielts_book.mapper;

import com.yib.your_ielts_book.dto.ResourceDTO;
import com.yib.your_ielts_book.model.Resource;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ResourceMapper {

    private final ModelMapper modelMapper;

    public ResourceMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    public ResourceDTO toResourceDTO(Resource resource) {
        return modelMapper.map(resource, ResourceDTO.class);
    }

    public Resource toResourceEntity(ResourceDTO resourceDTO) {
        return modelMapper.map(resourceDTO, Resource.class);
    }
}

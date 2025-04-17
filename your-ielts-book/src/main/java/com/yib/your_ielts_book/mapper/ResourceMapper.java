package com.yib.your_ielts_book.mapper;

import com.yib.your_ielts_book.dto.ResourceDTO;
import com.yib.your_ielts_book.model.Resource;

import java.util.HashSet;

public class ResourceMapper {
    //Mapping to resource dto
    public static Resource mapToResource(ResourceDTO resourceDTO) {
        return new Resource(
                resourceDTO.getResourceId(),
                resourceDTO.getTitle(),
                resourceDTO.getCategory(),
                resourceDTO.getDescription(),
                resourceDTO.getContent(),
                resourceDTO.getCreatedAt(),
                resourceDTO.getAuthor(),
                resourceDTO.getStatus(),
                resourceDTO.getType(),
                resourceDTO.getPrice(),
                new HashSet<>()
        );
    }

    //Mapping to resource
    public static ResourceDTO mapToResourceDTO(Resource resource) {
        return new ResourceDTO(
                resource.getResourceId(),
                resource.getTitle(),
                resource.getCategory(),
                resource.getDescription(),
                resource.getContent(),
                resource.getCreatedAt(),
                resource.getAuthor(),
                resource.getStatus(),
                resource.getType(),
                resource.getPrice()
        );
    }
}

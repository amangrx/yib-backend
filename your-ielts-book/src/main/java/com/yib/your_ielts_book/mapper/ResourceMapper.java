package com.yib.your_ielts_book.mapper;

import com.yib.your_ielts_book.dto.ResourceDTO;
import com.yib.your_ielts_book.model.Resource;

public class ResourceMapper {
    //Mapping to resource dto
    public static Resource mapToResource(ResourceDTO resourceDTO) {
        return new Resource(
                resourceDTO.getAdditionalResourceName(),
                resourceDTO.getAdditionalResourcePath(),
                resourceDTO.getAdditionalResourceType(),
                resourceDTO.getCategory(),
                resourceDTO.getFileName(),
                resourceDTO.getFilePath(),
                resourceDTO.getFileType(),
                resourceDTO.getResourceName(),
                resourceDTO.getResourceId()
        );
    }

    //Mapping to resource
    public static ResourceDTO mapToResourceDTO(Resource resource) {
        return new ResourceDTO(
                resource.getAdditionalResourceName(),
                resource.getAdditionalResourcePath(),
                resource.getAdditionalResourceType(),
                resource.getCategory(),
                resource.getFileName(),
                resource.getFilePath(),
                resource.getFileType(),
                resource.getResourceName(),
                resource.getResourceId()
        );
    }
}

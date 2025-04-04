package com.yib.your_ielts_book.service;

import com.yib.your_ielts_book.dto.ResourceDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ResourceService {

    List<ResourceDTO> getAllResource();

    ResourceDTO saveResource(ResourceDTO resourceDTO, MultipartFile resourceFile, MultipartFile additionalFile) throws IOException;

    void deleteResource(int resourceId);

    ResourceDTO getResourceById(int resourceId);
}

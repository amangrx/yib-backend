package com.yib.your_ielts_book.service;

import com.yib.your_ielts_book.dto.ResourceDTO;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ResourceService {

    List<ResourceDTO> getAllResource();
    void deleteResource(int resourceId);
//
//    ResourceDTO getResourceById(int resourceId);

    ResourceDTO createResource(@Valid ResourceDTO resourceDTO, String jwt);

    void updateStatus(int resourceId, String status);

    List<ResourceDTO> getApprovedResources();
}

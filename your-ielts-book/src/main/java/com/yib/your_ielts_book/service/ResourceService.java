package com.yib.your_ielts_book.service;

import com.yib.your_ielts_book.dto.ResourceDTO;
import com.yib.your_ielts_book.model.Resource;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ResourceService {

    List<ResourceDTO> getAllResource();
    void deleteResource(int resourceId);

    ResourceDTO createResource(@Valid ResourceDTO resourceDTO, String jwt);

    void updateStatus(int resourceId, String status);

    Page<ResourceDTO> getApprovedResources(Pageable pageable);

    List<ResourceDTO> getResourceByExpert(String author);

    ResourceDTO getResourceById(int resourceId);

    Page<Resource> searchByTitle(String title, PageRequest of);
}

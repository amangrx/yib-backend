package com.yib.your_ielts_book.service.impls;

import com.yib.your_ielts_book.config.JWTService;
import com.yib.your_ielts_book.dto.ResourceDTO;
import com.yib.your_ielts_book.exception.ResourceNotFoundException;
import com.yib.your_ielts_book.mapper.ResourceMapper;
import com.yib.your_ielts_book.model.Resource;
import com.yib.your_ielts_book.model.ResourceStatus;
import com.yib.your_ielts_book.repo.ResourceRepo;
import com.yib.your_ielts_book.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepo resourceRepo;
    private final JWTService jwtService;

    @Autowired
    public ResourceServiceImpl(ResourceRepo resourceRepo, JWTService jwtService) {
        this.resourceRepo = resourceRepo;
        this.jwtService = jwtService;
    }

    @Override
    public List<ResourceDTO> getAllResource() {
        List<Resource> resources = resourceRepo.findAll();
        return resources.stream()
                .map(ResourceMapper::mapToResourceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteResource(int resourceId) {
        Resource resource = resourceRepo.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));
        resourceRepo.delete(resource);
    }

    @Override
    public ResourceDTO createResource(ResourceDTO resourceDTO, String jwt) {
        try {
            if (jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);
            }
            String name = jwtService.extractFullName(jwt);

            Resource resource = ResourceMapper.mapToResource(resourceDTO);
            resource.setAuthor(name);                   // Set author from JWT
            resource.setStatus(ResourceStatus.PENDING); // Set default status to PENDING

            resource = resourceRepo.save(resource);
            return ResourceMapper.mapToResourceDTO(resource); // Return DTO
        } catch (Exception ex) {
            throw new RuntimeException("Error creating resource", ex);
        }
    }

    @Override
    public void updateStatus(int resourceId, String status) {
        // Validate status
        ResourceStatus newStatus;
        try {
            newStatus = ResourceStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + status);
        }
        // Find and update resource
        Resource resource = resourceRepo.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));
        resource.setStatus(newStatus);
        resourceRepo.save(resource);
    }

    @Override
    public Page<ResourceDTO> getApprovedResources(Pageable pageable) {
        Page<Resource> approvedResources = resourceRepo.findByStatus(ResourceStatus.APPROVED, pageable);
        return approvedResources.map(ResourceMapper::mapToResourceDTO);
    }

    @Override
    public List<ResourceDTO> getResourceByExpert(String author) {
        List<Resource> resources = resourceRepo.findByAuthor(author);
        return resources.stream()
                .map(ResourceMapper::mapToResourceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResourceDTO getResourceById(int resourceId) {
        Resource resources = resourceRepo.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));
        return ResourceMapper.mapToResourceDTO(resources);
    }

    @Override
    public Page<Resource> searchByTitle(String title, PageRequest of) {
        return resourceRepo.findByTitleContainingIgnoreCase(title, of);
    }
}

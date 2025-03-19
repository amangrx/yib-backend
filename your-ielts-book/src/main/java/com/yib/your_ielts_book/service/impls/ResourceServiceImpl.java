package com.yib.your_ielts_book.service.impls;

import com.yib.your_ielts_book.dto.ResourceDTO;
import com.yib.your_ielts_book.exception.ResourceNotFoundException;
import com.yib.your_ielts_book.mapper.ResourceMapper;
import com.yib.your_ielts_book.model.Resource;
import com.yib.your_ielts_book.repo.ResourceRepo;
import com.yib.your_ielts_book.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepo resourceRepo;

    @Value("${file.upload-dir}")
    private String uploadDirectory;

    @Autowired
    public ResourceServiceImpl(ResourceRepo resourceRepo) {
        this.resourceRepo = resourceRepo;
    }

    @Override
    public ResourceDTO saveResource(ResourceDTO resourceDTO, MultipartFile resourceFile, MultipartFile additionalFile) throws IOException {
        // Determine the category subdirectory
        String category = (resourceDTO.getCategory() != null && !resourceDTO.getCategory().isEmpty())
                ? resourceDTO.getCategory().toLowerCase().replaceAll("\\s+", "_") // Normalize category name
                : "uncategorized";

        // Create category-specific upload directory
        Path categoryDirectory = Paths.get(uploadDirectory, category);
        if (!Files.exists(categoryDirectory)) {
            Files.createDirectories(categoryDirectory);
        }

        // Save the main resource file
        String fileName = UUID.randomUUID() + "_" + resourceFile.getOriginalFilename();
        Path filePath = categoryDirectory.resolve(fileName);
        Files.write(filePath, resourceFile.getBytes());

        // Set main file details
        resourceDTO.setFileName(fileName);
        resourceDTO.setFileType(resourceFile.getContentType());
        resourceDTO.setFilePath(filePath.toString());

        // Save the additional file (if provided)
        if (additionalFile != null && !additionalFile.isEmpty()) {
            String additionalFileName = UUID.randomUUID() + "_" + additionalFile.getOriginalFilename();
            Path additionalFilePath = categoryDirectory.resolve(additionalFileName);
            Files.write(additionalFilePath, additionalFile.getBytes());

            // Set additional file details separately
            resourceDTO.setAdditionalResourceName(additionalFileName);
            resourceDTO.setAdditionalResourceType(additionalFile.getContentType());
            resourceDTO.setAdditionalResourcePath(additionalFilePath.toString());
        }

        // Convert DTO to Entity and save
        Resource resource = ResourceMapper.mapToResource(resourceDTO);
        resource = resourceRepo.save(resource);
        return ResourceMapper.mapToResourceDTO(resource);
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
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        // Delete the associated files from the storage
        try {
            if (resource.getFilePath() != null) {
                Files.deleteIfExists(Paths.get(resource.getFilePath()));
            }
            if (resource.getAdditionalResourcePath() != null) {
                Files.deleteIfExists(Paths.get(resource.getAdditionalResourcePath()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error deleting files from storage", e);
        }
        // Delete resource from database
        resourceRepo.delete(resource);
    }

    @Override
    public ResourceDTO getResourceById(int resourceId) throws ResourceNotFoundException {
        // Retrieve the resource entity from the database by ID
        Resource resource = resourceRepo.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));

        // Convert entity to DTO
        ResourceDTO resourceDTO = ResourceMapper.mapToResourceDTO(resource);

        // Verify that the file exists in the file system
        Path filePath = Paths.get(resource.getFilePath());
        if (!Files.exists(filePath)) {
            throw new ResourceNotFoundException("Resource file not found on disk: " + resource.getFilePath());
        }

        // If there's an additional resource, verify that file exists too
        if (resource.getAdditionalResourcePath() != null && !resource.getAdditionalResourcePath().isEmpty()) {
            Path additionalFilePath = Paths.get(resource.getAdditionalResourcePath());
            if (!Files.exists(additionalFilePath)) {
                throw new ResourceNotFoundException("Additional resource file not found on disk: " +
                        resource.getAdditionalResourcePath());
            }
        }
        return resourceDTO;
    }
}

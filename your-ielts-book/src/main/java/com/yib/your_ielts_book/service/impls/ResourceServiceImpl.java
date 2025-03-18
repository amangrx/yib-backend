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
        // Ensure the upload directory exists
        Path directory = Paths.get(uploadDirectory);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        // Save the main resource file
        String fileName = System.currentTimeMillis() + "_" + resourceFile.getOriginalFilename();
        Path filePath = directory.resolve(fileName);
        Files.write(filePath, resourceFile.getBytes());

        // Set main file details
        resourceDTO.setFileName(fileName);
        resourceDTO.setFileType(resourceFile.getContentType());
        resourceDTO.setFilePath(filePath.toString());

        // Save the additional file (if provided)
        if (additionalFile != null && !additionalFile.isEmpty()) {
            String additionalFileName = System.currentTimeMillis() + "_" + additionalFile.getOriginalFilename();
            Path additionalFilePath = directory.resolve(additionalFileName);
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
}

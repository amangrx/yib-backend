package com.yib.your_ielts_book.service.impls;

import com.yib.your_ielts_book.config.JWTService;
import com.yib.your_ielts_book.dto.ResourceDTO;
import com.yib.your_ielts_book.exception.ResourceNotFoundException;
import com.yib.your_ielts_book.mapper.ResourceMapper;
import com.yib.your_ielts_book.model.Resource;
import com.yib.your_ielts_book.model.ResourceStatus;
import com.yib.your_ielts_book.model.ResourceType;
import com.yib.your_ielts_book.repo.ResourceRepo;
import com.yib.your_ielts_book.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl implements ResourceService {

    private final String FILE_STORAGE_PATH = "src/main/resources/static/uploads/";
    private final String RES_DIR = "resources/";
    private final ResourceRepo resourceRepo;
    private final JWTService jwtService;
    private final ResourceMapper resourceMapper;

    @Autowired
    public ResourceServiceImpl(ResourceRepo resourceRepo, JWTService jwtService, ResourceMapper resourceMapper) {
        this.resourceRepo = resourceRepo;
        this.jwtService = jwtService;
        this.resourceMapper = resourceMapper;
    }

    @Override
    public List<ResourceDTO> getAllResource() {
        List<Resource> resources = resourceRepo.findAll();
        return resources.stream()
                .map(resourceMapper::toResourceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteResource(int resourceId) {
        Resource resource = resourceRepo.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));
        resourceRepo.delete(resource);
    }

    @Override
    public ResourceDTO createResource(ResourceDTO resourceDTO, MultipartFile pdfFile, String jwt) {
        try {
            if (jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);
            }
            int expertId = jwtService.extractUserId(jwt);
            String name = jwtService.extractFullName(jwt);
            System.out.println("s aaxa"+pdfFile.getOriginalFilename());
            System.out.println("s aaxa"+resourceDTO);

            createDirectoryIfNotExists(FILE_STORAGE_PATH + RES_DIR);

            resourceDTO.setExpertId(expertId);
            resourceDTO.setAuthor(name);
            resourceDTO.setStatus(ResourceStatus.PENDING);
            resourceDTO.setCreatedAt(new Date());

            String pdfFilePath = null;
            if (pdfFile != null && !pdfFile.isEmpty()) {
                pdfFilePath = saveFile(pdfFile, RES_DIR);
                resourceDTO.setPdfFilePath(pdfFilePath);
                resourceDTO.setPdfFileUrl("/api/files/" + RES_DIR + getFileNameFromPath(pdfFilePath));
            }

            Resource resource = resourceMapper.toResourceEntity(resourceDTO);
            resource = resourceRepo.save(resource);
            return resourceMapper.toResourceDTO(resource); // Return DTO
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
        return approvedResources.map(resourceMapper::toResourceDTO);
    }

    @Override
    public List<ResourceDTO> getResourceByExpertId(String jwt) {
        try {
            if (jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);
            }
            int expertId = jwtService.extractUserId(jwt);
            List<Resource> resources = resourceRepo.findByExpertId(expertId);
            return resources.stream()
                    .map(resourceMapper::toResourceDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error getting resource by expert ID.");
        }
    }

    @Override
    public ResourceDTO getResourceById(int resourceId) {
        Resource resources = resourceRepo.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));
        ResourceDTO resourceDTO = resourceMapper.toResourceDTO(resources);

        if (resources.getPdfFilePath() != null && !resources.getPdfFilePath().isEmpty()) {
            String fileName = getFileNameFromPath(resources.getPdfFilePath());
            resourceDTO.setPdfFileUrl("/api/files/" + RES_DIR + fileName);
        }

        return resourceDTO;
    }

    @Override
    public Page<ResourceDTO> searchByTitle(String title, PageRequest of) {
        Page<Resource> resources =  resourceRepo.findByTitleContainingIgnoreCase(title, of);
        return resources.map(resourceMapper::toResourceDTO);
    }

    @Override
    public Page<ResourceDTO> filterResources(String category, ResourceType type, Double minPrice, Double maxPrice, Pageable pageable) {
        Double effectiveMinPrice = minPrice != null ? minPrice : 0.0;
        Double effectiveMaxPrice = maxPrice != null ? maxPrice : Double.MAX_VALUE;

        Page<Resource> resources = resourceRepo.findByFilters(category, type, effectiveMinPrice, effectiveMaxPrice, pageable);

        return resources.map(resourceMapper::toResourceDTO);
    }

    private void createDirectoryIfNotExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    // Function to save the file.
    private String saveFile(MultipartFile file, String directory) {
        try {
            // Generate a unique filename to prevent overwrite
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            String relativePath = directory + uniqueFilename;
            String absolutePath = FILE_STORAGE_PATH + relativePath;

            // Save the file to the specified location
            Path path = Paths.get(absolutePath);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // Return the relative path to store in the database
            return relativePath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    private String getFileNameFromPath(String path) {
        if (path == null) return null;
        int lastSlashIndex = path.lastIndexOf("/");
        return lastSlashIndex != -1 ? path.substring(lastSlashIndex + 1) : path;
    }
}

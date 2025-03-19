package com.yib.your_ielts_book.controller;

import com.yib.your_ielts_book.dto.ResourceDTO;
import com.yib.your_ielts_book.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/yib/resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @PostMapping(path = "/add_resource")
    public ResponseEntity<ResourceDTO> addResource(@RequestPart("resource") ResourceDTO resourceDTO,
                                                   @RequestPart("resourceFile") MultipartFile resourceFile,
                                                   @RequestPart(value = "additionalFile", required = false) MultipartFile additionalFile) throws IOException {
        System.out.println("Received Resource: " + resourceDTO.getDifficultyLevel());
        ResourceDTO savedResource = resourceService.saveResource(resourceDTO, resourceFile, additionalFile);
        return new ResponseEntity<>(savedResource, HttpStatus.CREATED);
    }

    @GetMapping(path = "/get_resources")
    public ResponseEntity<List<ResourceDTO>> getResources() {
        List<ResourceDTO> resourceList = resourceService.getAllResource();
        return new ResponseEntity<>(resourceList, HttpStatus.OK);
    }

    @GetMapping(path = "/resource_details/{resourceId}")
    public ResponseEntity<ResourceDTO> getResourceById(@PathVariable("resourceId") int resourceId) {
        ResourceDTO resourceDTO = resourceService.getResourceById(resourceId);
        return ResponseEntity.ok(resourceDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteResource(@PathVariable("id") int resourceId) {
        resourceService.deleteResource(resourceId);
        return ResponseEntity.ok("Resource with ID " + resourceId + " has been deleted successfully.");
    }
}

package com.yib.your_ielts_book.controller;

import com.yib.your_ielts_book.dto.ResourceDTO;
import com.yib.your_ielts_book.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/yib/admin/resources")
public class AdminResourceController {

    @Autowired
    private ResourceService resourceService;

    @PostMapping(path = "/add_resource")
    public ResponseEntity<ResourceDTO> addResource(@RequestPart("resource") ResourceDTO resourceDTO,
                                                   @RequestPart("resourceFile") MultipartFile resourceFile,
                                                   @RequestPart(value = "additionalFile", required = false) MultipartFile additionalFile) throws IOException {
        ResourceDTO savedResource = resourceService.saveResource(resourceDTO, resourceFile, additionalFile);
        return new ResponseEntity<>(savedResource, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteResource(@PathVariable("id") int resourceId) {
        resourceService.deleteResource(resourceId);
        return ResponseEntity.ok("Resource with ID " + resourceId + " has been deleted successfully.");
    }
}

package com.yib.your_ielts_book.controller;

import com.yib.your_ielts_book.dto.ResourceDTO;
import com.yib.your_ielts_book.service.ResourceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/yib/expert")
public class ExpertController {
    @Autowired
    private ResourceService resourceService;

    @PostMapping("/resource")
    public ResponseEntity<ResourceDTO> createResources(@Valid @RequestBody ResourceDTO resourceDTO,
                                            @RequestHeader("Authorization") String jwt){
        try {
            ResourceDTO createdResource = resourceService.createResource(resourceDTO, jwt);
            return new ResponseEntity<>(createdResource, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

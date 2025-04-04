package com.yib.your_ielts_book.controller;

import com.yib.your_ielts_book.dto.ResourceDTO;
import com.yib.your_ielts_book.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/yib/resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

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


}

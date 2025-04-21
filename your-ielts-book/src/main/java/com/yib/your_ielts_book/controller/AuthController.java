package com.yib.your_ielts_book.controller;

import com.yib.your_ielts_book.dto.ResourceDTO;
import com.yib.your_ielts_book.service.AuthService;
import com.yib.your_ielts_book.service.CustomerResourceService;
import com.yib.your_ielts_book.service.ResourceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/yib/auth")
public class AuthController {

    private final AuthService authService;
    private final CustomerResourceService customerResourceService;
    private final ResourceService resourceService;

    public AuthController(AuthService authService, CustomerResourceService customerResourcService, ResourceService resourceService) {
        this.authService = authService;
        this.customerResourceService = customerResourcService;
        this.resourceService = resourceService;
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkUserDetails(HttpServletRequest request) {
        return authService.checkUserDetails(request);
    }

    @GetMapping("/check/pay/{resourceId}")
    public boolean checkPayStatus(@RequestHeader("Authorization") String jwt,
                                  @PathVariable("resourceId") int resourceId) {
        return customerResourceService.checkPayStatus(jwt, resourceId);
    }

    @GetMapping("/resource/{resourceId}")
    public ResponseEntity<ResourceDTO> getResourceById(@RequestHeader("Authorization") String jwt,
                                         @PathVariable("resourceId") int resourceId) {
        ResourceDTO resourceDetail = resourceService.getResourceById(resourceId);
        return new ResponseEntity<>(resourceDetail, HttpStatus.OK);
    }
}
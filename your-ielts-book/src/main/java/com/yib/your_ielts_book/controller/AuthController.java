package com.yib.your_ielts_book.controller;

import com.yib.your_ielts_book.dto.ResourceDTO;
import com.yib.your_ielts_book.dto.WritingQuestionDTO;
import com.yib.your_ielts_book.dto.WritingTestUserDTO;
import com.yib.your_ielts_book.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
    private final WritingQuestionService writingQuestionService;
    private final WritingTestUserService writingTestUserService;

    public AuthController(AuthService authService, CustomerResourceService customerResourcService, ResourceService resourceService, WritingQuestionService writingQuestionService, WritingTestUserService writingTestUserService) {
        this.authService = authService;
        this.customerResourceService = customerResourcService;
        this.resourceService = resourceService;
        this.writingQuestionService = writingQuestionService;
        this.writingTestUserService = writingTestUserService;
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

    @GetMapping("/answers/{questionId}")
    public ResponseEntity<WritingQuestionDTO> getWritingQuestionById(@PathVariable("questionId") int questionId,
                                                                     @RequestHeader("Authorization") String jwt) {
        WritingQuestionDTO answerDetail = writingQuestionService.getWritingQuestionById(questionId);
        System.out.println(answerDetail);
        return new ResponseEntity<>(answerDetail, HttpStatus.OK);
    }

    @PostMapping("/start/{questionId}")
    public ResponseEntity<WritingTestUserDTO> startWritingTest(@RequestHeader("Authorization") String jwt,
                                                               @PathVariable int questionId) {
        return ResponseEntity.ok(writingTestUserService.startWritingTest(questionId, jwt));
    }

    @PostMapping("/submit")
    public ResponseEntity<WritingTestUserDTO> submitWritingTest(@RequestHeader("Authorization") String jwt,
                                                                @RequestBody @Valid WritingTestUserDTO userAnswerDTO) {
        System.out.println("controller"+ userAnswerDTO);
        return ResponseEntity.ok(writingTestUserService.submitWritingTest(jwt, userAnswerDTO));
    }
}
package com.yib.your_ielts_book.controller;

import com.yib.your_ielts_book.dto.*;
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
    private final ReadingAndListeningQuestionService readingAndListeningQuestionService;
    private final ReadingAndListeningTestUserService readingAndListeningTestUserService;

    public AuthController(AuthService authService, CustomerResourceService customerResourcService, ResourceService resourceService, WritingQuestionService writingQuestionService, WritingTestUserService writingTestUserService, ReadingAndListeningQuestionService readingAndListeningQuestionService, ReadingAndListeningTestUserService readingAndListeningTestUserService) {
        this.authService = authService;
        this.customerResourceService = customerResourcService;
        this.resourceService = resourceService;
        this.writingQuestionService = writingQuestionService;
        this.writingTestUserService = writingTestUserService;
        this.readingAndListeningQuestionService = readingAndListeningQuestionService;
        this.readingAndListeningTestUserService = readingAndListeningTestUserService;
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

    @GetMapping("/detail/{questionId}")
    public ResponseEntity<WritingQuestionDTO> getWritingQuestionById(@PathVariable("questionId") int questionId,
                                                                     @RequestHeader("Authorization") String jwt) {
        WritingQuestionDTO detail = writingQuestionService.getWritingQuestionById(questionId);
        return new ResponseEntity<>(detail, HttpStatus.OK);
    }

    @PostMapping("/submit")
    public ResponseEntity<WritingTestUserDTO> submitWritingTest(@RequestHeader("Authorization") String jwt,
                                                                @RequestBody @Valid WritingTestUserDTO userAnswerDTO) {
        return ResponseEntity.ok(writingTestUserService.submitWritingTest(jwt, userAnswerDTO));
    }

    @GetMapping("/reading-listening-answer/{questionId}")
    public ResponseEntity<ReadingAndListeningQuestionDTO> getReadingAndListeningQuestionById(@PathVariable("questionId") int questionId,
                                                                                 @RequestHeader("Authorization") String jwt) {
        ReadingAndListeningQuestionDTO answerDetail = readingAndListeningQuestionService.getReadingAndListeningQuestionsById(questionId);
        return new ResponseEntity<>(answerDetail, HttpStatus.OK);
    }

    @PostMapping("/reading-listening/submit")
    public ResponseEntity<ReadingAndListeningTestUserDTO> submitReadingOrListeningTest(@RequestHeader("Authorization") String jwt,
                                                                @RequestBody @Valid ReadingAndListeningTestUserDTO userAnswerDTO) {
        System.out.println("controller ma "+ userAnswerDTO);
        return ResponseEntity.ok(readingAndListeningTestUserService.submitReadingOrListeningTest(jwt, userAnswerDTO));
    }
}
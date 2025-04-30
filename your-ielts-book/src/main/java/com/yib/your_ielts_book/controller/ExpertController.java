package com.yib.your_ielts_book.controller;

import com.yib.your_ielts_book.dto.*;
import com.yib.your_ielts_book.service.QuestionService;
import com.yib.your_ielts_book.service.ResourceService;
import com.yib.your_ielts_book.service.WritingTestUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/yib/expert")
public class ExpertController {
    @Autowired
    private ResourceService resourceService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private WritingTestUserService writingTestUserService;

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

    @GetMapping("/my-uploads")
    public ResponseEntity<List<ResourceDTO>> getMyUploads(@RequestHeader("Authorization") String jwt){
        List<ResourceDTO> myUploadsList = resourceService.getResourceByExpertId(jwt);
        return new ResponseEntity<>(myUploadsList, HttpStatus.OK);
    }

    @PostMapping(value = "/writing", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WritingQuestionDTO> createWritingQues(@ModelAttribute WritingQuestionDTO dto,
                                                                @RequestHeader("Authorization") String jwt){
        WritingQuestionDTO writingQuestion = questionService.createWritingQues(dto, jwt);
        return new ResponseEntity<>(writingQuestion, HttpStatus.CREATED);
    }

    @PostMapping(value = "/reading-listening", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReadingAndListeningQuestionDTO> creatingReadingListeningQues(@ModelAttribute ReadingAndListeningQuestionDTO dto,
                                                                            @RequestPart("pdfFile") MultipartFile pdfFile,
                                                                            @RequestPart(value = "audioFile", required = false) MultipartFile audioFile,
                                                                            @RequestHeader("Authorization") String jwt){
        ReadingAndListeningQuestionDTO createdQues = questionService.creatingReadingListeningQues(dto, pdfFile, audioFile, jwt);
        return ResponseEntity.ok(createdQues);
    }

    @GetMapping("/all/tests")
    public List<ReviewWritingTestDTO> getAllTestByExpert(@RequestHeader("Authorization") String jwt){
        return writingTestUserService.getTestsForExpertReview(jwt);
    }

    @GetMapping("/test/{testId}")
    public ResponseEntity<WritingTestAnswerResponseDTO> getUserTestAnswer(@RequestHeader("Authorization") String jwt,
                                              @PathVariable("testId") int testId){
        WritingTestAnswerResponseDTO responseDTO = writingTestUserService.getWritingTestAnswer(testId);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/{testId}/review")
    public ResponseEntity<ReviewResponseDTO> submitTestReview(@PathVariable("testId") int testId,
                                                                     @Valid @RequestBody ReviewWritingRequestDTO reviewDTO,
                                                                     @RequestHeader("Authorization") String jwt){
        ReviewResponseDTO response = writingTestUserService.submitTestReview(testId, reviewDTO, jwt);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

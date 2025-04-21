package com.yib.your_ielts_book.controller;

import com.yib.your_ielts_book.dto.ResourceDTO;
import com.yib.your_ielts_book.dto.WritingQuestionDTO;
import com.yib.your_ielts_book.model.WritingQuestion;
import com.yib.your_ielts_book.service.QuestionService;
import com.yib.your_ielts_book.service.ResourceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/yib/expert")
public class ExpertController {
    @Autowired
    private ResourceService resourceService;

    @Autowired
    private QuestionService questionService;

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

    @GetMapping("/{name}/my-uploads")
    public ResponseEntity<List<ResourceDTO>> getMyUploads(@RequestHeader("Authorization") String jwt,
                                                          @PathVariable("name") String author){
        List<ResourceDTO> myUploadsList = resourceService.getResourceByExpert(author);
        return new ResponseEntity<>(myUploadsList, HttpStatus.OK);
    }

    @PostMapping(value = "/writing", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WritingQuestionDTO> createWritingQues(@ModelAttribute WritingQuestionDTO dto,
                                                                @RequestHeader("Authorization") String jwt){
        WritingQuestionDTO writingQuestion = questionService.createWritingQues(dto, jwt);
        System.out.println("controller" + writingQuestion);
        return new ResponseEntity<>(writingQuestion, HttpStatus.CREATED);
    }
}

package com.yib.your_ielts_book.controller;

import com.yib.your_ielts_book.dto.*;
import com.yib.your_ielts_book.model.Resource;
import com.yib.your_ielts_book.response.ResponseMessage;
import com.yib.your_ielts_book.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/yib/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TestimonialService testimonialService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private WritingQuestionService writingQuestionService;

    @Autowired
    private ReadingAndListeningQuestionService readingAndListeningQuestionService;

    //Register customer REST API
    @PostMapping(path = "/register")
    public ResponseEntity<?> registerNewCustomer(@RequestPart("customerDTO") @Valid CustomerDTO customerDTO,
                                                 @RequestPart("profilePicture") MultipartFile profilePicture)  {
        try{
            return new ResponseEntity<>(customerService.registerNewCustomer(customerDTO, profilePicture), HttpStatus.CREATED);
        }catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //Login customer REST API
    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(@Valid @RequestBody LoginDTO loginDTO) throws IOException {
        ResponseMessage response = customerService.LoginCustomer(loginDTO);

        if (response.isSuccess()) {
            String token = response.getToken();
            return ResponseEntity.ok(token); // HTTP 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); // HTTP 401 Unauthorized
        }
    }

    @GetMapping(path = "/resources")
    public ResponseEntity<Page<ResourceDTO>> getApprovedResources(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ResourceDTO> approvedResourcePage = resourceService.getApprovedResources(PageRequest.of(page, size));
        return new ResponseEntity<>(approvedResourcePage, HttpStatus.OK);
    }

    //Contact us REST API
    @PostMapping("/comments")
    public ResponseEntity<ResponseMessage> addTestimonial(@Valid @RequestBody TestimonialDTO testimonialDTO) {
        ResponseMessage response = testimonialService.addTestimonial(testimonialDTO);
        return response.getSuccess() ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Resource>> searchResources(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        Page<Resource> resources = resourceService.searchByTitle(title, PageRequest.of(page, size));
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/test/writing")
    public ResponseEntity<List<WritingQuestionDTO>> getAllWritingQuestions() {
        List<WritingQuestionDTO> questions = writingQuestionService.getAllWritingQuestions();
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/test/reading-listening")
    public ResponseEntity<List<ReadingAndListeningQuestionDTO>> getAllReadingAndListeningQuestions() {
        List<ReadingAndListeningQuestionDTO> questions = readingAndListeningQuestionService.getAllReadingAndListeningQuestions();
        return ResponseEntity.ok(questions);
    }
}


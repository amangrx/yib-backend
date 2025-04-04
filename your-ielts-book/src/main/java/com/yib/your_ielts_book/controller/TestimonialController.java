package com.yib.your_ielts_book.controller;

import com.yib.your_ielts_book.dto.TestimonialDTO;
import com.yib.your_ielts_book.response.ResponseMessage;
import com.yib.your_ielts_book.service.TestimonialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/yib/testimonial")
public class TestimonialController {
    @Autowired
    public TestimonialService testimonialService;

    @PostMapping
    public ResponseEntity<ResponseMessage> addTestimonial(@Valid @RequestBody TestimonialDTO testimonialDTO) {
        ResponseMessage response = testimonialService.addTestimonial(testimonialDTO);
        return response.getStatus() ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}

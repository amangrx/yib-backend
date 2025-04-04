package com.yib.your_ielts_book.service;


import com.yib.your_ielts_book.dto.TestimonialDTO;
import com.yib.your_ielts_book.response.ResponseMessage;

public interface TestimonialService {

    ResponseMessage addTestimonial(TestimonialDTO testimonialDTO);
}

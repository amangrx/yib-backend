package com.yib.your_ielts_book.service;


import com.yib.your_ielts_book.dto.TestimonialDTO;
import com.yib.your_ielts_book.response.ResponseMessage;

import java.util.List;

public interface TestimonialService {

    ResponseMessage addTestimonial(TestimonialDTO testimonialDTO);

    List<TestimonialDTO> getTestimonials(String jwt);
}

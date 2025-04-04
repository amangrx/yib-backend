package com.yib.your_ielts_book.mapper;

import com.yib.your_ielts_book.dto.TestimonialDTO;
import com.yib.your_ielts_book.model.Testimonial;

public class TestimonialMapper {
    public static Testimonial mapToTestimonial(TestimonialDTO testimonialDTO) {
        return new Testimonial(
                testimonialDTO.getTestimonialId(),
                testimonialDTO.getFullName(),
                testimonialDTO.getEmail(),
                testimonialDTO.getPhoneNumber(),
                testimonialDTO.getUploadedDate(),
                testimonialDTO.getComments()
        );
    }

    public static TestimonialDTO mapToTestimonialDTO(Testimonial testimonial) {
        return new TestimonialDTO(
                testimonial.getTestimonialId(),
                testimonial.getFullName(),
                testimonial.getEmail(),
                testimonial.getPhoneNumber(),
                testimonial.getUploadedDate(),
                testimonial.getComments()
        );
    }
}

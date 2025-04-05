package com.yib.your_ielts_book.service.impls;

import com.yib.your_ielts_book.dto.TestimonialDTO;
import com.yib.your_ielts_book.mapper.TestimonialMapper;
import com.yib.your_ielts_book.model.Testimonial;
import com.yib.your_ielts_book.repo.TestimonialRepo;
import com.yib.your_ielts_book.response.ResponseMessage;
import com.yib.your_ielts_book.service.TestimonialService;
import org.springframework.stereotype.Service;

@Service
public class TestimonialServiceImpl implements TestimonialService{

    private final TestimonialRepo testimonialRepo;

    public TestimonialServiceImpl(TestimonialRepo testimonialRepo) {
        this.testimonialRepo = testimonialRepo;
    }

    @Override
    public ResponseMessage addTestimonial(TestimonialDTO testimonialDTO) {
        try {
            Testimonial testimonial = TestimonialMapper.mapToTestimonial(testimonialDTO);
            testimonialRepo.save(testimonial);
            return new ResponseMessage("Your comment has been submitted", true, null);
        } catch (Exception e) {
            return new ResponseMessage("An error occurred.", false, null);
        }
    }

}

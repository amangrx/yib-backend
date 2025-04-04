package com.yib.your_ielts_book.repo;

import com.yib.your_ielts_book.model.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestimonialRepo extends JpaRepository<Testimonial, Integer> {
}

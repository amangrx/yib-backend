package com.yib.your_ielts_book.repo;

import com.yib.your_ielts_book.model.WritingTestUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WritingTestUserRepo extends JpaRepository<WritingTestUser, Integer> {
    Optional<WritingTestUser> findByIdAndCustomerId(int id, int customerId);
    Optional<WritingTestUser> findByCustomerIdAndWritingQuestionId(int customerId, int questionId);
}

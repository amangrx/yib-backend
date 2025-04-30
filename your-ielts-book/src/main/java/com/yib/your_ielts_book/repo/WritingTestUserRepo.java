package com.yib.your_ielts_book.repo;

import com.yib.your_ielts_book.model.TestStatus;
import com.yib.your_ielts_book.model.WritingTestUser;
import com.yib.your_ielts_book.projection.ReviewTestProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WritingTestUserRepo extends JpaRepository<WritingTestUser, Integer> {
    Optional<WritingTestUser> findByIdAndCustomerId(int id, int customerId);
    Optional<WritingTestUser> findByCustomerIdAndWritingQuestionId(int customerId, int questionId);
    @Query("SELECT wt FROM WritingTestUser wt JOIN wt.writingQuestion q WHERE q.expertId = :expertId")
    List<WritingTestUser> findByExpertId(@Param("expertId") int expertId);

    @Query("SELECT wt FROM WritingTestUser wt JOIN wt.writingQuestion q WHERE q.expertId = :expertId AND wt.status = :status")
    List<WritingTestUser> findByExpertIdAndStatus(@Param("expertId") int expertId, @Param("status") TestStatus status);

    @Query("SELECT wt.id as testId, t.customerId as customerId, u.name as customerName, " +
            "t.submittedAt as submittedAt, t.status as status, " +
            "t.questionId as questionId, t.questionCategory as questionCategory, " +
            "t.duration as duration, " +
            "CASE WHEN wt.answer IS NOT NULL THEN true ELSE false END as hasAnswer " +
            "FROM WritingTestUser wt " +
            "JOIN Test t ON wt.id = t.id " +
            "JOIN Customer u ON t.customerId = u.customerId " +
            "JOIN WritingQuestion wq ON t.questionId = wq.id " +
            "WHERE wq.expertId = :expertId")
    List<ReviewTestProjection> findTestsForExpertReview(@Param("expertId") int expertId);
}

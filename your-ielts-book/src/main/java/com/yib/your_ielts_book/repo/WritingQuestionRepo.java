package com.yib.your_ielts_book.repo;

import com.yib.your_ielts_book.model.WritingQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@EnableJpaRepositories
@Transactional
public interface WritingQuestionRepo extends JpaRepository<WritingQuestion, Integer> {
}

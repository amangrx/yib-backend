package com.yib.your_ielts_book.repo;

import com.yib.your_ielts_book.model.ReadingAndListeningTestUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface ReadingAndListeningTestUserRepo extends JpaRepository<ReadingAndListeningTestUser, Integer> {
}

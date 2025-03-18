package com.yib.your_ielts_book.repo;

import com.yib.your_ielts_book.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepo extends JpaRepository<Resource, Integer> {
}

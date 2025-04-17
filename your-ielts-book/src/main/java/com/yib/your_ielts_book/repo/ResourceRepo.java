package com.yib.your_ielts_book.repo;

import com.yib.your_ielts_book.model.Resource;
import com.yib.your_ielts_book.model.ResourceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepo extends JpaRepository<Resource, Integer> {
    List<Resource> findByStatus(ResourceStatus status);
}

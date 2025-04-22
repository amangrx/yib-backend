package com.yib.your_ielts_book.repo;

import com.yib.your_ielts_book.model.Resource;
import com.yib.your_ielts_book.model.ResourceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepo extends JpaRepository<Resource, Integer> {
    Page<Resource> findByStatus(ResourceStatus status, Pageable pageable);

    List<Resource> findByExpertId(int expertId);

    Page<Resource> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}

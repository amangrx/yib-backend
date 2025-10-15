package com.yib.your_ielts_book.repo;

import com.yib.your_ielts_book.model.Resource;
import com.yib.your_ielts_book.model.ResourceStatus;
import com.yib.your_ielts_book.model.ResourceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepo extends JpaRepository<Resource, Integer> {
    Page<Resource> findByStatus(ResourceStatus status, Pageable pageable);

    List<Resource> findByExpertId(int expertId);

    Page<Resource> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT r FROM Resource r WHERE " +
            "(:category IS NULL OR LOWER(r.category) = LOWER(:category)) AND " +
            "(:type IS NULL OR r.type = :type) AND " +
            "(:minPrice IS NULL OR r.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR r.price <= :maxPrice)")
    Page<Resource> findByFilters(
            @Param("category") String category,
            @Param("type") ResourceType type,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable);
}

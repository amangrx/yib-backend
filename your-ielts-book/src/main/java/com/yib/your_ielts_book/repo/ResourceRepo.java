package com.yib.your_ielts_book.repo;

import com.yib.your_ielts_book.model.Resource;
import com.yib.your_ielts_book.model.ResourceStatus;
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
    List<Resource> findByAuthor(String author);

    // Basic paginated search by title
//    Page<Resource> findByTitleContainingIgnoreCase(String title, Pageable pageable);
//
//    // Advanced paginated search with custom query
//    @Query("SELECT r FROM Resource r WHERE " +
//            "LOWER(r.title) LIKE LOWER(CONCAT('%', :query, '%')) AND " +
//            "r.status = 'PUBLISHED'") // Only search published resources
//    Page<Resource> searchPublishedByTitle(
//            @Param("query") String query,
//            Pageable pageable);
//
//    // Search with multiple fields
//    @Query("SELECT r FROM Resource r WHERE " +
//            "(LOWER(r.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
//            "LOWER(r.description) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
//            "r.status = 'PUBLISHED'")
//    Page<Resource> searchPublishedByTitleOrDescription(
//            @Param("query") String query,
//            Pageable pageable);

    Page<Resource> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}

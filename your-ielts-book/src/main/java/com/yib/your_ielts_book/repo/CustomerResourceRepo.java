package com.yib.your_ielts_book.repo;

import com.yib.your_ielts_book.model.Customer;
import com.yib.your_ielts_book.model.CustomerResource;
import com.yib.your_ielts_book.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface CustomerResourceRepo extends JpaRepository<CustomerResource, Integer> {
    @Query("SELECT cr FROM CustomerResource cr WHERE cr.customer.customerId = :customerId AND cr.resource.resourceId = :resourceId")
    Optional<CustomerResource> findByCustomerAndResource(
            @Param("customerId") int customerId,
            @Param("resourceId") int resourceId
    );
    boolean existsByCustomerAndResource(Customer customer, Resource resource);
}

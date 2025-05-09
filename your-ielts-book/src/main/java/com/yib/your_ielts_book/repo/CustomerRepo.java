package com.yib.your_ielts_book.repo;

import com.yib.your_ielts_book.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByEmailAndPassword(String email, String password);
}

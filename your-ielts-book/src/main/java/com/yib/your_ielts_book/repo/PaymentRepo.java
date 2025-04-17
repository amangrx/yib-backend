package com.yib.your_ielts_book.repo;

import com.yib.your_ielts_book.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface PaymentRepo extends JpaRepository<Payment, Integer> {
}

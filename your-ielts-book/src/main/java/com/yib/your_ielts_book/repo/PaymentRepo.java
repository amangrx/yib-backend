package com.yib.your_ielts_book.repo;

import com.yib.your_ielts_book.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface PaymentRepo extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByTransactionId(String transactionId);
    Optional<Payment> findByPidx(String pidx);
    Optional<Payment> findByKhaltiTransactionId(String khaltiTransactionId);

//    List<Payment> findByCustomerId(Integer customerId);
//    List<Payment> findByResourceId(Integer resourceId);
//    List<Payment> findByStatus(String status);
}

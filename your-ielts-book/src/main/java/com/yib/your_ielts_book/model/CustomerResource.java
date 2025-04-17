package com.yib.your_ielts_book.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_resources")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)  // Changed from "id" to "customer_id"
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", nullable = false)  // Changed to consistent naming
    private Resource resource;

    @ManyToOne(fetch = FetchType.LAZY)  // Changed from @OneToOne
    @JoinColumn(name = "payment_id")  // Changed from "paymentId"
    private Payment payment;

    @Column(nullable = false)
    private LocalDateTime accessGrantedAt;

    private LocalDateTime accessExpiresAt;

    @Column(nullable = false)
    private boolean isFreeAccess;

    @PrePersist
    protected void onCreate() {
        if (this.accessGrantedAt == null) {
            this.accessGrantedAt = LocalDateTime.now();
        }
    }
}
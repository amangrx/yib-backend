package com.yib.your_ielts_book.service.impls;

import com.yib.your_ielts_book.config.JWTService;
import com.yib.your_ielts_book.exception.ResourceNotFoundException;
import com.yib.your_ielts_book.model.Customer;
import com.yib.your_ielts_book.model.CustomerResource;
import com.yib.your_ielts_book.model.Resource;
import com.yib.your_ielts_book.repo.CustomerRepo;
import com.yib.your_ielts_book.repo.CustomerResourceRepo;
import com.yib.your_ielts_book.repo.ResourceRepo;
import com.yib.your_ielts_book.service.CustomerResourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CustomerResourceServiceImpl implements CustomerResourceService {

    private final CustomerResourceRepo customerResourceRepo;
    private final JWTService jwtService;
    private final CustomerRepo customerRepo;
    private final ResourceRepo resourceRepo;

    public CustomerResourceServiceImpl(CustomerResourceRepo customerResourceRepo, JWTService jwtService, CustomerRepo customerRepo, ResourceRepo resourceRepo) {
        this.customerResourceRepo = customerResourceRepo;
        this.jwtService = jwtService;
        this.customerRepo = customerRepo;
        this.resourceRepo = resourceRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkPayStatus(String jwt, int resourceId) {
        try {
            if (jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);
            }
            int customerId = jwtService.extractUserId(jwt);

            System.out.println("Checking payment status for customer ID: " + customerId + ", resource ID: " + resourceId);

            // Get the actual Customer and Resource entities
            Customer customer = customerRepo.findById(customerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

            Resource resource = resourceRepo.findById(resourceId)
                    .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

            // Use the entities instead of IDs
            Optional<CustomerResource> customerResourceOpt = customerResourceRepo.findByCustomerAndResource(customer, resource);

            if (customerResourceOpt.isEmpty()) {
                System.out.println("No customer resource found for customer: " + customerId + ", resource: " + resourceId);
                return false;
            }

            CustomerResource customerResource = customerResourceOpt.get();
            System.out.println("Customer resource found - Free access: " + customerResource.isFreeAccess() +
                    ", Access expires at: " + customerResource.getAccessExpiresAt());

            // Check if it's free access
            if (customerResource.isFreeAccess()) {
                System.out.println("Access granted - Free access");
                return true;
            }

            // Check if there's a payment and it hasn't expired
            if (customerResource.getPayment() != null) {
                System.out.println("Payment found - Transaction ID: " + customerResource.getPayment().getTransactionId());

                if (customerResource.getAccessExpiresAt() == null ||
                        customerResource.getAccessExpiresAt().isAfter(LocalDateTime.now())) {
                    System.out.println("Access granted - Payment valid and not expired");
                    return true;
                } else {
                    System.out.println("Access denied - Payment expired");
                }
            } else {
                System.out.println("No payment found for this customer resource");
            }

            return false;

        } catch (Exception e) {
            System.err.println("Error checking payment status: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error checking payment status: " + e.getMessage(), e);
        }
    }

}

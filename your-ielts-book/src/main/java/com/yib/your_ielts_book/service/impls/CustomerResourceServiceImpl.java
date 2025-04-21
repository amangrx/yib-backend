package com.yib.your_ielts_book.service.impls;

import com.yib.your_ielts_book.config.JWTService;
import com.yib.your_ielts_book.repo.CustomerResourceRepo;
import com.yib.your_ielts_book.service.CustomerResourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CustomerResourceServiceImpl implements CustomerResourceService {

    private final CustomerResourceRepo customerResourceRepo;
    private final JWTService jwtService;

    public CustomerResourceServiceImpl(CustomerResourceRepo customerResourceRepo, JWTService jwtService) {
        this.customerResourceRepo = customerResourceRepo;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkPayStatus(String jwt, int resourceId) {
        try {
            if (jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);
            }
            int customerId = jwtService.extractUserId(jwt);
            return customerResourceRepo.findByCustomerAndResource(customerId, resourceId)
                    .map(customerResource -> {
                        if (customerResource.isFreeAccess()) {
                            return true;
                        }

                        if (customerResource.getPayment() != null) {
                            if (customerResource.getAccessExpiresAt() == null ||
                                    customerResource.getAccessExpiresAt().isAfter(LocalDateTime.now())) {
                                return true;
                            }
                        }
                        return false;
                    })
                    .orElse(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

package com.yib.your_ielts_book.service;

import com.yib.your_ielts_book.model.Customer;
import com.yib.your_ielts_book.repo.CustomerRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final CustomerRepo customerRepo;

    public CustomUserDetailService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Customer> customer = customerRepo.findByEmail(email);
        if (customer.isEmpty()) {
            throw new UsernameNotFoundException("Customer not found");
        }
        Customer customerDetails = customer.get();
        return User.builder()
                .username(customerDetails.getEmail())
                .password(customerDetails.getPassword())
                .roles(customerDetails.getRole().name())
                .build();
    }
}

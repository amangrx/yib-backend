package com.yib.your_ielts_book.service.impls;

import com.yib.your_ielts_book.dto.CustomerDTO;
import com.yib.your_ielts_book.dto.LoginDTO;
import com.yib.your_ielts_book.model.Customer;
import com.yib.your_ielts_book.repo.CustomerRepo;
import com.yib.your_ielts_book.response.LoginResponse;
import com.yib.your_ielts_book.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo;
    private final PasswordEncoder passwordEncoder ;

    @Autowired
    public CustomerServiceImpl(CustomerRepo customerRepo, PasswordEncoder passwordEncoder) {
        this.customerRepo = customerRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String registerNewCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer(
                customerDTO.getCustomerId(),
                customerDTO.getAddress(),
                customerDTO.getName(),
                customerDTO.getCustomerType(),
                customerDTO.getEmail(),
                customerDTO.getPhoneNumber(),
                this.passwordEncoder.encode(customerDTO.getPassword())
        );
        customerRepo.save(customer);
        return "Customer " + customer.getName() + " has been successfully registered!";
    }

    @Override
    public LoginResponse LoginCustomer(LoginDTO loginDTO) {
        Optional<Customer> customer = customerRepo.findByEmail(loginDTO.getEmail());
        if (customer.isPresent()) {
            Customer customerDB = customer.get();
            Boolean passwordMatch = passwordEncoder.matches(loginDTO.getPassword(), customerDB.getPassword());
            if (passwordMatch) {
                return new LoginResponse("Login successful.", true);
            }else {
                return new LoginResponse("Login failed.", false);
            }
        }else{
            return new LoginResponse("Email does not exist.", false);
        }
    }
}

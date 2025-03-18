package com.yib.your_ielts_book.service.impls;

import com.yib.your_ielts_book.dto.CustomerDTO;
import com.yib.your_ielts_book.dto.LoginDTO;
import com.yib.your_ielts_book.exception.ResourceNotFoundException;
import com.yib.your_ielts_book.mapper.CustomerMapper;
import com.yib.your_ielts_book.model.Customer;
import com.yib.your_ielts_book.repo.CustomerRepo;
import com.yib.your_ielts_book.response.LoginResponse;
import com.yib.your_ielts_book.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Customer customer = CustomerMapper.mapToCustomer(customerDTO);
        customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        customerRepo.save(customer);
        return "Customer " + customer.getName() + " has been successfully registered!";
    }

    @Override
    public List<CustomerDTO> getAllCustomer() {
        List<Customer> customers = customerRepo.findAll();
        return customers
                .stream().map(CustomerMapper::mapToCustomerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCustomer(int customerId) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Customer not found"));
        customerRepo.delete(customer);
    }

    @Override
    public LoginResponse LoginCustomer(LoginDTO loginDTO) {
        Optional<Customer> customer = customerRepo.findByEmail(loginDTO.getEmail());
        if (customer.isPresent()) {
            Customer customerDB = customer.get();
            boolean passwordMatch = passwordEncoder.matches(loginDTO.getPassword(), customerDB.getPassword());
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

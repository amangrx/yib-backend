package com.yib.your_ielts_book.service.impls;

import com.yib.your_ielts_book.config.JWTService;
import com.yib.your_ielts_book.dto.CustomerDTO;
import com.yib.your_ielts_book.dto.LoginDTO;
import com.yib.your_ielts_book.exception.ResourceAlreadyExistsException;
import com.yib.your_ielts_book.exception.ResourceNotFoundException;
import com.yib.your_ielts_book.mapper.CustomerMapper;
import com.yib.your_ielts_book.model.Customer;
import com.yib.your_ielts_book.model.UserRole;
import com.yib.your_ielts_book.repo.CustomerRepo;
import com.yib.your_ielts_book.response.ResponseMessage;
import com.yib.your_ielts_book.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo;
    private final PasswordEncoder passwordEncoder ;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Autowired
    public CustomerServiceImpl(CustomerRepo customerRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.customerRepo = customerRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    //Method to register new customer.
    @Override
    public String registerNewCustomer(CustomerDTO customerDTO, MultipartFile profilePicture) throws IOException {
        // Checking for existing email and phone number
        boolean emailExists = customerRepo.existsByEmail(customerDTO.getEmail());
        boolean phoneNoExists = customerRepo.existsByPhoneNumber(customerDTO.getPhoneNumber());

        if (emailExists) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }
        if (phoneNoExists) {
            throw new ResourceAlreadyExistsException("Phone number already exists");
        }

        // Map DTO to Entity
        Customer customer = CustomerMapper.mapToCustomer(customerDTO);
        customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));

        // Upload profile image if provided
        if (profilePicture != null && !profilePicture.isEmpty()) {
            customer.setPictureName(profilePicture.getOriginalFilename());
            customer.setPictureType(profilePicture.getContentType());
            customer.setProfilePicture(profilePicture.getBytes());
        }

        // Set default role to CUSTOMER
        customer.setRole(UserRole.CUSTOMER);

        // Save to database
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
    public ResponseMessage LoginCustomer(LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
            );

            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(loginDTO.getEmail());
                return new ResponseMessage("Login successful. Token: ", true, token);
            } else {
                return new ResponseMessage("Authentication failed.", false, null);
            }
        } catch (AuthenticationException e) {
            return new ResponseMessage("Invalid email or password.", false, null);
        }
    }

}

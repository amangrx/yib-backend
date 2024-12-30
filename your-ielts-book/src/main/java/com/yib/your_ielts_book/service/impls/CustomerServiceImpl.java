package com.yib.your_ielts_book.service.impls;

import com.yib.your_ielts_book.dto.CustomerDTO;
import com.yib.your_ielts_book.model.Customer;
import com.yib.your_ielts_book.repo.CustomerRepo;
import com.yib.your_ielts_book.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo;

    @Autowired
    public CustomerServiceImpl(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
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
                customerDTO.getPassword()
        );
        customerRepo.save(customer);
        return "Customer " + customer.getName() + " has been successfully registered!";
    }
}

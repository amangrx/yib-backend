package com.yib.your_ielts_book.mapper;

import com.yib.your_ielts_book.dto.CustomerDTO;
import com.yib.your_ielts_book.model.Customer;

public class CustomerMapper {
    public static Customer mapToCustomer(CustomerDTO customerDTO) {
        return new Customer(
                customerDTO.getCustomerId(),
                customerDTO.getAddress(),
                customerDTO.getName(),
                "User",
                customerDTO.getEmail(),
                customerDTO.getPhoneNumber(),
                customerDTO.getPassword()
        );
    }

    public static CustomerDTO mapToCustomerDTO(Customer customer) {
        return new CustomerDTO(
                customer.getCustomerId(),
                customer.getAddress(),
                customer.getName(),
                customer.getCustomerType(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getPassword()
        );
    }
}

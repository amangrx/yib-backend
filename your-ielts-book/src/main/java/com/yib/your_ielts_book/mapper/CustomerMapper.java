package com.yib.your_ielts_book.mapper;

import com.yib.your_ielts_book.dto.CustomerDTO;
import com.yib.your_ielts_book.model.Customer;

public class CustomerMapper {
    public static Customer mapToCustomer(CustomerDTO customerDTO) {
        return new Customer(
                customerDTO.getCustomerId(),
                customerDTO.getAddress(),
                customerDTO.getEmail(),
                customerDTO.getName(),
                customerDTO.getPassword(),
                customerDTO.getPhoneNumber(),
                customerDTO.getPictureName(),
                customerDTO.getPictureType(),
                customerDTO.getProfilePicture(),
                null
        );
    }

    public static CustomerDTO mapToCustomerDTO(Customer customer) {
        return new CustomerDTO(
                customer.getCustomerId(),
                customer.getAddress(),
                customer.getEmail(),
                customer.getName(),
                customer.getPassword(),
                customer.getPhoneNumber(),
                customer.getPictureName(),
                customer.getPictureType(),
                customer.getProfilePicture(),
                customer.getRole()
        );
    }
}

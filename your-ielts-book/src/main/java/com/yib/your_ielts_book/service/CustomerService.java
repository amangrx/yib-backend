package com.yib.your_ielts_book.service;

import com.yib.your_ielts_book.dto.CustomerDTO;
import com.yib.your_ielts_book.dto.LoginDTO;
import com.yib.your_ielts_book.response.LoginResponse;

import java.util.List;

public interface CustomerService {

    LoginResponse LoginCustomer(LoginDTO loginDTO);

    String registerNewCustomer(CustomerDTO customerDTO);

    List<CustomerDTO> getAllCustomer();
    void deleteCustomer(int customerId);
}

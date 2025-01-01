package com.yib.your_ielts_book.service;

import com.yib.your_ielts_book.dto.CustomerDTO;
import com.yib.your_ielts_book.dto.LoginDTO;
import com.yib.your_ielts_book.response.LoginResponse;

public interface CustomerService {

    LoginResponse LoginCustomer(LoginDTO loginDTO);

    String registerNewCustomer(CustomerDTO customerDTO);
}

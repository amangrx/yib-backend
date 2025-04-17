package com.yib.your_ielts_book.service;

import com.yib.your_ielts_book.dto.CustomerDTO;
import com.yib.your_ielts_book.dto.LoginDTO;
import com.yib.your_ielts_book.response.ResponseMessage;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CustomerService {

    ResponseMessage LoginCustomer(LoginDTO loginDTO) throws IOException;

    String registerNewCustomer(CustomerDTO customerDTO, MultipartFile profilePicture) throws IOException;

    List<CustomerDTO> getAllCustomer();
    void deleteCustomer(int customerId);
}

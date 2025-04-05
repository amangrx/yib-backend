package com.yib.your_ielts_book.controller;

import com.yib.your_ielts_book.dto.CustomerDTO;
import com.yib.your_ielts_book.dto.LoginDTO;
import com.yib.your_ielts_book.response.ResponseMessage;
import com.yib.your_ielts_book.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/yib/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    //Register customer REST API
    @PostMapping(path = "/register")
    public ResponseEntity<?> registerNewCustomer(@RequestPart("customerDTO") CustomerDTO customerDTO,
                                                 @RequestPart("profilePicture") MultipartFile profilePicture)  {
        try{
            return new ResponseEntity<>(customerService.registerNewCustomer(customerDTO, profilePicture), HttpStatus.CREATED);
        }catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //Login customer REST API
    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(@RequestBody LoginDTO loginDTO) {
        ResponseMessage response = customerService.LoginCustomer(loginDTO);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response); // HTTP 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); // HTTP 401 Unauthorized
        }
    }

}

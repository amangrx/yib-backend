package com.yib.your_ielts_book.controller;

import com.yib.your_ielts_book.dto.CustomerDTO;
import com.yib.your_ielts_book.dto.LoginDTO;
import com.yib.your_ielts_book.response.LoginResponse;
import com.yib.your_ielts_book.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/yib/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    //Register customer REST API
    @PostMapping(path = "/register")
    public ResponseEntity<?> registerNewCustomer(@RequestBody CustomerDTO customerDTO) {
        return new ResponseEntity<>(customerService.registerNewCustomer(customerDTO), HttpStatus.CREATED);
    }

    //Login customer REST API
    @PostMapping(path = "/login")
    public ResponseEntity<?> loginCustomer(@RequestBody LoginDTO loginDTO) {
        LoginResponse loginResponse = customerService.LoginCustomer(loginDTO);
        return ResponseEntity.ok(loginResponse);
    }

    //Get all customer REST API
    @GetMapping(path = "/list-customers")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customerDTOList = customerService.getAllCustomer();
        return ResponseEntity.ok(customerDTOList);
    }

    //Delete user REST API
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("id") int customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.ok("Customer deleted successfully!.");
    }
}

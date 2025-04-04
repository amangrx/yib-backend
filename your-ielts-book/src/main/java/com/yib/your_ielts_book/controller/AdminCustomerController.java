package com.yib.your_ielts_book.controller;

import com.yib.your_ielts_book.dto.CustomerDTO;
import com.yib.your_ielts_book.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/yib/admin")
public class AdminCustomerController {

    @Autowired
    private CustomerService customerService;

//    Get all customer REST API
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

package com.yib.your_ielts_book.controller;

import com.yib.your_ielts_book.dto.CustomerDTO;
import com.yib.your_ielts_book.dto.LoginDTO;
import com.yib.your_ielts_book.dto.ResourceDTO;
import com.yib.your_ielts_book.dto.TestimonialDTO;
import com.yib.your_ielts_book.response.ResponseMessage;
import com.yib.your_ielts_book.service.CustomerService;
import com.yib.your_ielts_book.service.ResourceService;
import com.yib.your_ielts_book.service.TestimonialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/yib/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TestimonialService testimonialService;

    @Autowired
    private ResourceService resourceService;

    //Register customer REST API
    @PostMapping(path = "/register")
    public ResponseEntity<?> registerNewCustomer(@RequestPart("customerDTO") @Valid CustomerDTO customerDTO,
                                                 @RequestPart("profilePicture") MultipartFile profilePicture)  {
        try{
            return new ResponseEntity<>(customerService.registerNewCustomer(customerDTO, profilePicture), HttpStatus.CREATED);
        }catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //Login customer REST API
    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(@Valid @RequestBody LoginDTO loginDTO) throws IOException {
        ResponseMessage response = customerService.LoginCustomer(loginDTO);

        if (response.isSuccess()) {
            String token = response.getToken();
            return ResponseEntity.ok(token); // HTTP 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); // HTTP 401 Unauthorized
        }
    }

    @GetMapping(path = "/resources")
    public ResponseEntity<List<ResourceDTO>> getApprovedResources() {
        List<ResourceDTO> approvedResourceList = resourceService.getApprovedResources();
        return new ResponseEntity<>(approvedResourceList, HttpStatus.OK);
    }
    //REST API to get all resources for library.
//    @GetMapping(path = "/resources")
//    public ResponseEntity<List<ResourceDTO>> getResources() {
//        List<ResourceDTO> resourceList = resourceService.getAllResource();
//        return new ResponseEntity<>(resourceList, HttpStatus.OK);
//    }

    //Contact us REST API
    @PostMapping("/comments")
    public ResponseEntity<ResponseMessage> addTestimonial(@Valid @RequestBody TestimonialDTO testimonialDTO) {
        ResponseMessage response = testimonialService.addTestimonial(testimonialDTO);
        return response.getSuccess() ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}

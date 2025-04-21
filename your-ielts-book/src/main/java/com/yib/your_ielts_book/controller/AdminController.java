package com.yib.your_ielts_book.controller;

import com.yib.your_ielts_book.dto.ResourceDTO;
import com.yib.your_ielts_book.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/yib/admin")
public class AdminController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping(path = "/resources")
    public ResponseEntity<List<ResourceDTO>> getResources(@RequestHeader("Authorization") String jwt) {
        List<ResourceDTO> resourceList = resourceService.getAllResource();
        return new ResponseEntity<>(resourceList, HttpStatus.OK);
    }

    @DeleteMapping("/resources/delete/{resourceId}")
    public ResponseEntity<String> deleteResource(@RequestHeader("Authorization")String jwt,
                                                 @PathVariable("resourceId") int resourceId) {
        resourceService.deleteResource(resourceId);
        return ResponseEntity.ok("Resource with ID " + resourceId + " has been deleted successfully.");
    }

    @PutMapping(path = "/resources/update/{resourceId}/{status}")  // Changed ${} to {}
    public ResponseEntity<String> updateResourceStatus(@RequestHeader("Authorization") String jwt,
                                                       @PathVariable("resourceId") int resourceId,
                                                       @PathVariable("status") String status) {
        resourceService.updateStatus(resourceId, status);
        return ResponseEntity.ok("Resource with ID " + resourceId + " has been updated successfully.");
    }

    //    Get all customer REST API
//    @GetMapping(path = "/list-customers")
//    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
//        List<CustomerDTO> customerDTOList = customerService.getAllCustomer();
//        return ResponseEntity.ok(customerDTOList);
//    }
//
//    //Delete user REST API
//    @DeleteMapping("{id}")
//    public ResponseEntity<String> deleteCustomer(@PathVariable("id") int customerId) {
//        customerService.deleteCustomer(customerId);
//        return ResponseEntity.ok("Customer deleted successfully!.");
//    }

}

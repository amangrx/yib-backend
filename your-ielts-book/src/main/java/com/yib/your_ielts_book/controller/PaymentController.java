package com.yib.your_ielts_book.controller;

import com.yib.your_ielts_book.payload.KhaltiPaymentRequest;
import com.yib.your_ielts_book.payload.PaymentResponse;
import com.yib.your_ielts_book.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/yib/payments")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class PaymentController{
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("khalti/initiate/{resourceId}")
    public ResponseEntity<PaymentResponse> initiatePayment(@PathVariable("resourceId") int resourceId,
                                                           @Valid @RequestBody KhaltiPaymentRequest request,
                                                           @RequestHeader("Authorization") String jwt) {
        System.out.println("controller ma"+request);
        PaymentResponse response = paymentService.initiatePayment(request, jwt, resourceId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/khalti/verify")
    public ResponseEntity<PaymentResponse> verifyPayment(@RequestParam String pidx) {
        System.out.println("controller ma" + pidx);
        PaymentResponse response = paymentService.verifyPayment(pidx);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
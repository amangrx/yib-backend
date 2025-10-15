package com.yib.your_ielts_book.controller;

import com.yib.your_ielts_book.exception.PaymentException;
import com.yib.your_ielts_book.exception.ResourceNotFoundException;
import com.yib.your_ielts_book.model.Payment;
import com.yib.your_ielts_book.payload.KhaltiPaymentRequest;
import com.yib.your_ielts_book.payload.PaymentResponse;
import com.yib.your_ielts_book.repo.PaymentRepo;
import com.yib.your_ielts_book.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/yib/payments")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentRepo paymentRepo;

    public PaymentController(PaymentService paymentService, PaymentRepo paymentRepo) {
        this.paymentService = paymentService;
        this.paymentRepo = paymentRepo;
    }

    @PostMapping("khalti/initiate/{resourceId}")
    public ResponseEntity<PaymentResponse> initiatePayment(@PathVariable("resourceId") int resourceId, @Valid @RequestBody KhaltiPaymentRequest request, @RequestHeader("Authorization") String jwt) {
        System.out.println("controller ma" + request);
        PaymentResponse response = paymentService.initiatePayment(request, jwt, resourceId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    @GetMapping("/khalti/verify")
//    public ResponseEntity<PaymentResponse> verifyPayment(@RequestParam String pidx) {
//        try {
//            System.out.println("Controller received pidx: " + pidx);
//
//            // Validate pidx parameter
//            if (pidx == null || pidx.trim().isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//            }
//
//            PaymentResponse response = paymentService.verifyPayment(pidx.trim());
//            return new ResponseEntity<>(response, HttpStatus.OK);
//
//        } catch (PaymentException e) {
//            System.err.println("Payment verification failed: " + e.getMessage());
//            return new ResponseEntity<>(HttpStatus.PAYMENT_REQUIRED);
//        } catch (Exception e) {
//            System.err.println("Unexpected error during payment verification: " + e.getMessage());
//            e.printStackTrace();
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("/khalti/verify")
    public ResponseEntity<?> verifyPayment(@RequestParam String pidx) {
        try {
            System.out.println("Controller received pidx: " + pidx);

            // Validate pidx parameter
            if (pidx == null || pidx.trim().isEmpty()) {
                System.err.println("ERROR: PIDX is null or empty");
                return ResponseEntity.badRequest().body(Map.of("error", "PIDX parameter is required", "status", "failed"));
            }

            PaymentResponse response = paymentService.verifyPayment(pidx.trim());
            System.out.println("Payment verification successful for pidx: " + pidx);
            return ResponseEntity.ok(response);

        } catch (PaymentException e) {
            System.err.println("PaymentException in controller: " + e.getMessage());

            // Check if it's a "already processed" error - this should be treated as success
            if (e.getMessage().contains("already processed") || e.getMessage().contains("already granted")) {

                // Try to get the payment details and return success response
                try {
                    Payment payment = paymentRepo.findByPidx(pidx).orElse(null);
                    if (payment != null && payment.getKhaltiTransactionId() != null) {
                        PaymentResponse response = new PaymentResponse();
                        response.setPidx(pidx);
                        response.setTransactionId(payment.getTransactionId());
                        response.setKhaltiTransactionId(payment.getKhaltiTransactionId());
                        response.setStatus("Completed");
                        response.setAmount(payment.getAmount());
                        response.setResourceTitle(payment.getResource().getTitle());
                        response.setCustomerName(payment.getCustomer().getName());

                        return ResponseEntity.ok(response);
                    }
                } catch (Exception ex) {
                    System.err.println("Failed to create success response for already processed payment: " + ex.getMessage());
                }
            }

            // Return detailed error for actual failures
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(Map.of("error", e.getMessage(), "status", "payment_failed", "pidx", pidx != null ? pidx : "null"));

        } catch (ResourceNotFoundException e) {
            System.err.println("ResourceNotFoundException in controller: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage(), "status", "not_found", "pidx", pidx != null ? pidx : "null"));

        } catch (Exception e) {
            System.err.println("Unexpected error in payment controller: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Internal server error: " + e.getMessage(), "status", "server_error", "pidx", pidx != null ? pidx : "null"));
        }
    }
}
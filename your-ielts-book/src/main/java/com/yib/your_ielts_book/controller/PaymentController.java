//package com.yib.your_ielts_book.controller;
//
//import com.stripe.Stripe;
//import com.stripe.exception.StripeException;
//import com.stripe.model.Event;
//import com.stripe.model.PaymentIntent;
//import com.stripe.net.Webhook;
//import com.stripe.param.PaymentIntentCreateParams;
//import com.yib.your_ielts_book.model.Customer;
//import com.yib.your_ielts_book.model.CustomerResource;
//import com.yib.your_ielts_book.model.Payment;
//import com.yib.your_ielts_book.model.Resource;
//import com.yib.your_ielts_book.repo.CustomerRepo;
//import com.yib.your_ielts_book.repo.CustomerResourceRepo;
//import com.yib.your_ielts_book.repo.PaymentRepo;
//import com.yib.your_ielts_book.repo.ResourceRepo;
//import lombok.Data;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/payments")
//public class PaymentController {
//
//    @Value("${stripe.secret-key}")
//    private String stripeSecretKey;
//
//    private final PaymentRepo paymentRepository;
//    private final ResourceRepo resourceRepository;
//    private final CustomerRepo customerRepository;
//    private final CustomerResourceRepo customerResourceRepo;
//
//    public PaymentController(PaymentRepo paymentRepository, ResourceRepo resourceRepository, CustomerRepo customerRepository, CustomerResourceRepo customerResourceRepository) {
//        this.paymentRepository = paymentRepository;
//        this.resourceRepository = resourceRepository;
//        this.customerRepository = customerRepository;
//        this.customerResourceRepo = customerResourceRepository;
//    }
//
//    @PostMapping("/create-payment-intent")
//    public ResponseEntity<Map<String, String>> createPaymentIntent(
//            @RequestBody PaymentRequest request,
//            @AuthenticationPrincipal UserDetails userDetails) {
//
//        // Get customer and resource
//        Customer customer = customerRepository.findByEmail(userDetails.getUsername())
//                .orElseThrow(() -> new RuntimeException("Customer not found"));
//        Resource resource = resourceRepository.findById(request.getResourceId())
//                .orElseThrow(() -> new RuntimeException("Resource not found"));
//
//        // Create payment record
//        Payment payment = new Payment();
//        payment.setCustomer(customer);
//        payment.setResource(resource);
//        payment.setAmount(resource.getPrice());
//        payment.setStatus(PaymentStatus.PENDING);
//        payment.setCurrency("NPR");
//        payment = paymentRepository.save(payment);
//
//        // Create Stripe PaymentIntent
//        Stripe.apiKey = stripeSecretKey;
//
//        Map<String, String> metadata = new HashMap<>();
//        metadata.put("payment_id", payment.getId().toString());
//        metadata.put("resource_id", resource.getResourceId().toString());
//        metadata.put("customer_id", customer.getCustomerId().toString());
//
//        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
//                .setAmount((long)(resource.getPrice() * 100)) // in paisa
//                .setCurrency("npr")
//                .setCustomer(customer.getEmail())
//                .putAllMetadata(metadata)
//                .setAutomaticPaymentMethods(
//                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
//                                .setEnabled(true)
//                                .build())
//                .build();
//
//        try {
//            PaymentIntent paymentIntent = PaymentIntent.create(params);
//
//            // Update payment with Stripe ID
//            payment.setTransactionId(paymentIntent.getId());
//            paymentRepository.save(payment);
//
//            return ResponseEntity.ok(Map.of(
//                    "clientSecret", paymentIntent.getClientSecret(),
//                    "paymentId", payment.getId().toString()
//            ));
//        } catch (StripeException e) {
//            throw new RuntimeException("Error creating payment intent", e);
//        }
//    }
//
//    @PostMapping("/webhook")
//    public ResponseEntity<String> handleStripeWebhook(
//            @RequestBody String payload,
//            @RequestHeader("Stripe-Signature") String sigHeader) {
//
//        String endpointSecret = "whsec_your_webhook_secret";
//        Event event = null;
//
//        try {
//            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Invalid signature");
//        }
//
//        if ("payment_intent.succeeded".equals(event.getType())) {
//            PaymentIntent paymentIntent = (PaymentIntent) event.getData().getObject();
//
//            // Update payment status
//            Payment payment = paymentRepository.findByTransactionId(paymentIntent.getId())
//                    .orElseThrow(() -> new RuntimeException("Payment not found"));
//
//            payment.setStatus(PaymentStatus.SUCCEEDED);
//            paymentRepository.save(payment);
//
//            // Grant resource access
//            CustomerResource customerResource = new CustomerResource();
//            customerResource.setCustomer(payment.getCustomer());
//            customerResource.setResource(payment.getResource());
//            customerResource.setPayment(payment);
//            customerResource.setFreeAccess(false);
//            customerResource.setAccessExpiresAt(LocalDateTime.now().plusYears(1));
//            customerResourceRepo.save(customerResource);
//        }
//
//        return ResponseEntity.ok("Success");
//    }
//}
//
//@Data
//class PaymentRequest {
//    private Integer resourceId;
//}
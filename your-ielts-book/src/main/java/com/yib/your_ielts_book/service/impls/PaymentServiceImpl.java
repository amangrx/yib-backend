package com.yib.your_ielts_book.service.impls;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yib.your_ielts_book.config.JWTService;
import com.yib.your_ielts_book.exception.PaymentException;
import com.yib.your_ielts_book.exception.ResourceAlreadyExistsException;
import com.yib.your_ielts_book.exception.ResourceNotFoundException;
import com.yib.your_ielts_book.model.Customer;
import com.yib.your_ielts_book.model.CustomerResource;
import com.yib.your_ielts_book.model.Payment;
import com.yib.your_ielts_book.model.Resource;
import com.yib.your_ielts_book.payload.KhaltiPaymentRequest;
import com.yib.your_ielts_book.payload.PaymentResponse;
import com.yib.your_ielts_book.repo.CustomerRepo;
import com.yib.your_ielts_book.repo.CustomerResourceRepo;
import com.yib.your_ielts_book.repo.PaymentRepo;
import com.yib.your_ielts_book.repo.ResourceRepo;
import com.yib.your_ielts_book.service.PaymentService;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Value("${khalti.secret-key}")
    private String khaltiSecretKey;

    @Value("${khalti.base-url}")
    private String khaltiBaseUrl;

    private final PaymentRepo paymentRepo;
    private final CustomerRepo customerRepo;
    private final ResourceRepo resourceRepo;
    private final CustomerResourceRepo customerResourceRepo;
    private final JWTService jwtService;
    private final ObjectMapper objectMapper;

    public PaymentServiceImpl(PaymentRepo paymentRepo, CustomerRepo customerRepo, ResourceRepo resourceRepo, CustomerResourceRepo customerResourceRepo, JWTService jwtService, ObjectMapper objectMapper) {
        this.paymentRepo = paymentRepo;
        this.customerRepo = customerRepo;
        this.resourceRepo = resourceRepo;
        this.customerResourceRepo = customerResourceRepo;
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }

    @Override
    public PaymentResponse initiatePayment(KhaltiPaymentRequest request, String jwt, int resourceId) {
        try {
            if (jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);
            }
            int customerId = jwtService.extractUserId(jwt);

            Customer customer = customerRepo.findById(customerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

            Resource resource = resourceRepo.findById(resourceId)
                    .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

            if (customerResourceRepo.existsByCustomerAndResource(customer, resource)) {
                throw new ResourceAlreadyExistsException("You already have access to this resource");
            }

            // Create payment with internal transaction ID
            Payment payment = new Payment();
            payment.setCustomer(customer);
            payment.setResource(resource);
            payment.setAmount(request.getAmount()/100.0);
            payment.setTransactionId(UUID.randomUUID().toString());
            payment = paymentRepo.save(payment);

            try {
                // Call Khalti API to initiate payment
                String khaltiResponse = callKhaltiApi(request, payment, resource, customer);

                // Extract pidx from Khalti response and store it in our payment record
                String pidx = extractJsonField(khaltiResponse, "pidx");
                payment.setPidx(pidx);
                paymentRepo.save(payment);

                // Create response
                PaymentResponse response = new PaymentResponse();
                response.setPidx(pidx);
                response.setPaymentUrl(extractJsonField(khaltiResponse, "payment_url"));
                response.setTransactionId(payment.getTransactionId());
                response.setStatus("Initiated");
                response.setAmount(payment.getAmount());
                response.setResourceTitle(resource.getTitle());
                response.setCustomerName(customer.getName());
                return response;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PaymentResponse verifyPayment(String pidx) {
        try {
            System.out.println("Service received pidx: " + pidx);

            // Call Khalti and get verification response
            String verificationResponse = callKhaltiVerification(pidx);
            System.out.println("Raw verification response: " + verificationResponse);

            // Extract and validate status
            String status = extractJsonField(verificationResponse, "status");
            if (!"Completed".equals(status)) {
                throw new PaymentException("Payment not completed. Status: " + status);
            }

            // Extract pidx from response (in case it's different)
            String responsePidx = extractJsonField(verificationResponse, "pidx");
            if (pidx == null || pidx.isEmpty()) {
                pidx = responsePidx;
            }

            // Extract Khalti transaction ID
            String khaltiTransactionId = extractJsonField(verificationResponse, "transaction_id");

            // Try to find payment record by pidx first
            Payment payment = paymentRepo.findByPidx(pidx)
                    .orElseThrow(() -> new PaymentException("Payment record not found for pidx: "));

            // Update payment with Khalti-provided IDs for future reference
            payment.setKhaltiTransactionId(khaltiTransactionId);
            paymentRepo.save(payment);

            grantResourceAccess(payment);

            PaymentResponse response = new PaymentResponse();
            response.setPidx(pidx);
            response.setTransactionId(payment.getTransactionId());
            response.setKhaltiTransactionId(khaltiTransactionId);
            response.setStatus("Completed");
            response.setAmount(payment.getAmount());
            response.setResourceTitle(payment.getResource().getTitle());
            response.setCustomerName(payment.getCustomer().getName());

            return response;
        } catch (Exception e) {
            throw new PaymentException("Payment verification failed: " + e.getMessage());
        }
    }

    private String callKhaltiApi(KhaltiPaymentRequest request, Payment payment, Resource resource, Customer customer) throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(khaltiBaseUrl + "/epayment/initiate/");
            post.setHeader("Authorization", "Key " + khaltiSecretKey);
            post.setHeader("Content-Type", "application/json");

            String requestBody = objectMapper.writeValueAsString(new KhaltiRequestWrapper(
                    request.getReturnUrl(),
                    request.getReturnUrl().replace("/success", ""),
                    request.getAmount(),
                    payment.getTransactionId(),
                    resource.getTitle(),
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhoneNumber()
            ));

            post.setEntity(new StringEntity(requestBody));
            return client.execute(post, response -> EntityUtils.toString(response.getEntity()));
        }
    }

    private String callKhaltiVerification(String pidx) throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(khaltiBaseUrl + "/epayment/lookup/");
            post.setHeader("Authorization", "Key " + khaltiSecretKey);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity("{\"pidx\":\"" + pidx + "\"}"));
            return client.execute(post, response -> EntityUtils.toString(response.getEntity()));
        }
    }

    private void grantResourceAccess(Payment payment) {
        if (customerResourceRepo.existsByCustomerAndResource(payment.getCustomer(), payment.getResource())) {
            throw new PaymentException("Resource access already granted");
        }

        CustomerResource customerResource = new CustomerResource();
        customerResource.setCustomer(payment.getCustomer());
        customerResource.setResource(payment.getResource());
        customerResource.setPayment(payment);
        customerResource.setAccessGrantedAt(LocalDateTime.now());
        customerResource.setAccessExpiresAt(LocalDateTime.now().plusYears(1));
        customerResource.setFreeAccess(false);

        customerResourceRepo.save(customerResource);
    }

    private String extractJsonField(String json, String field) throws Exception {
        return objectMapper.readTree(json).get(field).asText();
    }

    // Helper class for Khalti request
    private record KhaltiRequestWrapper(
            String return_url,
            String website_url,
            int amount,
            String purchase_order_id,
            String purchase_order_name,
            String name,
            String email,
            String phone
    ) {}
}


package com.yib.your_ielts_book.service.impls;

import com.fasterxml.jackson.databind.JsonNode;
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
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public PaymentServiceImpl(PaymentRepo paymentRepo, CustomerRepo customerRepo, ResourceRepo resourceRepo,
                              CustomerResourceRepo customerResourceRepo, JWTService jwtService, ObjectMapper objectMapper) {
        this.paymentRepo = paymentRepo;
        this.customerRepo = customerRepo;
        this.resourceRepo = resourceRepo;
        this.customerResourceRepo = customerResourceRepo;
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
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
            payment.setAmount(request.getAmount() / 100.0);
            payment.setTransactionId(UUID.randomUUID().toString());
            payment = paymentRepo.save(payment);

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
            throw new PaymentException("Payment initiation failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public PaymentResponse verifyPayment(String pidx) {
        try {
            System.out.println("Service received pidx: " + pidx);

            // Validate pidx parameter
            if (pidx == null || pidx.trim().isEmpty()) {
                throw new PaymentException("PIDX parameter is required");
            }

            // Find payment record by pidx
            Payment payment = paymentRepo.findByPidx(pidx)
                    .orElseThrow(() -> new PaymentException("Payment record not found for pidx: " + pidx));

            // Check if payment is already completed
            if (payment.getStatus() == Payment.PaymentStatus.COMPLETED) {
                throw new PaymentException("Payment already processed");
            }

            // Call Khalti and get verification response
            String verificationResponse = callKhaltiVerification(pidx);
            System.out.println("Raw verification response: " + verificationResponse);

            // Parse the JSON response
            JsonNode responseNode = objectMapper.readTree(verificationResponse);

            // Check if the response contains an error
            if (responseNode.has("error_key") || responseNode.has("error")) {
                String errorMessage = responseNode.has("error_key") ?
                        responseNode.get("error_key").asText() :
                        responseNode.get("error").asText();

                // Update payment status to failed
                payment.setStatus(Payment.PaymentStatus.FAILED);
                paymentRepo.save(payment);

                throw new PaymentException("Khalti verification error: " + errorMessage);
            }

            // Extract and validate status
            String khaltiStatus = extractJsonField(verificationResponse, "status");
            System.out.println("Payment status from Khalti: " + khaltiStatus);

            if (!"Completed".equalsIgnoreCase(khaltiStatus)) {
                // Update payment status based on Khalti response
                if ("Pending".equalsIgnoreCase(khaltiStatus)) {
                    payment.setStatus(Payment.PaymentStatus.PENDING);
                } else {
                    payment.setStatus(Payment.PaymentStatus.FAILED);
                }
                paymentRepo.save(payment);
                throw new PaymentException("Payment not completed. Status: " + khaltiStatus);
            }

            // Extract Khalti transaction ID
            String khaltiTransactionId = extractJsonField(verificationResponse, "transaction_id");

            // Verify payment hasn't already been processed (double-check with resource access)
            if (customerResourceRepo.existsByCustomerAndResource(payment.getCustomer(), payment.getResource())) {
                payment.setStatus(Payment.PaymentStatus.COMPLETED);
                paymentRepo.save(payment);
                throw new PaymentException("Payment already processed - resource access already granted");
            }

            // Update payment status and details
            payment.setKhaltiTransactionId(khaltiTransactionId);
            payment.setStatus(Payment.PaymentStatus.COMPLETED);
            paymentRepo.save(payment);

            // Grant resource access
            grantResourceAccess(payment);

            // Create response
            PaymentResponse response = new PaymentResponse();
            response.setPidx(pidx);
            response.setTransactionId(payment.getTransactionId());
            response.setKhaltiTransactionId(khaltiTransactionId);
            response.setStatus("Completed");
            response.setAmount(payment.getAmount());
            response.setResourceTitle(payment.getResource().getTitle());
            response.setCustomerName(payment.getCustomer().getName());

            return response;

        } catch (PaymentException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Payment verification error: " + e.getMessage());
            e.printStackTrace();
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

            System.out.println("Khalti initiate request: " + requestBody);
            post.setEntity(new StringEntity(requestBody));

            try (CloseableHttpResponse response = client.execute(post)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                System.out.println("Khalti initiate response: " + responseBody);

                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    throw new PaymentException("Khalti API error: " + response.getStatusLine().getReasonPhrase());
                }

                return responseBody;
            }
        }
    }

    private String callKhaltiVerification(String pidx) throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(khaltiBaseUrl + "/epayment/lookup/");
            post.setHeader("Authorization", "Key " + khaltiSecretKey);
            post.setHeader("Content-Type", "application/json");

            String requestBody = "{\"pidx\":\"" + pidx + "\"}";
            System.out.println("Khalti verification request: " + requestBody);
            post.setEntity(new StringEntity(requestBody));

            try (CloseableHttpResponse response = client.execute(post)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                System.out.println("Khalti verification response status: " + response.getStatusLine().getStatusCode());
                System.out.println("Khalti verification response body: " + responseBody);

                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    throw new PaymentException("Khalti verification API error: " + response.getStatusLine().getReasonPhrase());
                }

                return responseBody;
            }
        }
    }

    private void grantResourceAccess(Payment payment) {
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
        JsonNode node = objectMapper.readTree(json);
        JsonNode fieldNode = node.get(field);

        if (fieldNode == null) {
            throw new PaymentException("Required field '" + field + "' not found in response");
        }

        return fieldNode.asText();
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
    ) {
    }
}
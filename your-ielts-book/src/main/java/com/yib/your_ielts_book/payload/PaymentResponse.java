package com.yib.your_ielts_book.payload;

import lombok.Data;

@Data
public class PaymentResponse {
    private String pidx;
    private String paymentUrl;
    private String transactionId;
    private String khaltiTransactionId;
    private String status;
    private String message;
    private Double amount;
    private String resourceTitle;
    private String customerName;
}
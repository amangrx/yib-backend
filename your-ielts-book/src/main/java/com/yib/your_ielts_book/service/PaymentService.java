package com.yib.your_ielts_book.service;

import com.yib.your_ielts_book.payload.KhaltiPaymentRequest;
import com.yib.your_ielts_book.payload.PaymentResponse;

public interface PaymentService {
    PaymentResponse initiatePayment(KhaltiPaymentRequest request, String jwt, int resourceId);

    PaymentResponse verifyPayment(String pidx);
}

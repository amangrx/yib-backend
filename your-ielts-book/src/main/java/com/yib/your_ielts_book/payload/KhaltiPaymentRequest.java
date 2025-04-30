package com.yib.your_ielts_book.payload;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class KhaltiPaymentRequest {

    @NotBlank(message = "Return URL is required")
    private String returnUrl;

    @Min(value = 100, message = "Amount must be at least 100 paisa (1 NPR)")
    private int amount;

    @NotBlank(message = "Purchase order ID is required")
    private String purchaseOrderId;

    @NotBlank(message = "Purchase order name is required")
    private String purchaseOrderName;

    @Valid
    @NotNull(message = "Customer info is required")
    private CustomerInfo customerInfo;

    @Data
    public static class CustomerInfo {
        @NotBlank(message = "Name is required")
        private String name;

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        private String email;

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
        private String phone;
    }
}
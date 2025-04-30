package com.yib.your_ielts_book.dto;

import lombok.Data;

@Data
public class ReviewResponseDTO {
    private int testId;
    private String status;
    private String message;
}
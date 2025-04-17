package com.yib.your_ielts_book.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> checkUserDetails(HttpServletRequest request);
}

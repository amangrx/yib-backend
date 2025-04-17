package com.yib.your_ielts_book.service.impls;

import com.yib.your_ielts_book.config.JWTService;
import com.yib.your_ielts_book.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final JWTService jwtService;

    public AuthServiceImpl(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public ResponseEntity<?> checkUserDetails(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);

            int customerId = jwtService.extractUserId(jwtToken);
            String fullName = jwtService.extractFullName(jwtToken);
            String role = jwtService.extractRole(jwtToken);

            if (fullName != null && role != null) {
                Map<String, Object> userDetails = new HashMap<>();  // Changed to Object
                userDetails.put("customerId", customerId);  // int allowed
                userDetails.put("name", fullName);
                userDetails.put("role", role);
                System.out.println(userDetails);
                return ResponseEntity.ok(userDetails);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();  // Handle error case
    }
}



package com.yib.your_ielts_book.config;

import com.yib.your_ielts_book.model.Customer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.token.validity}")
    private long jwtTokenValidity;

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof Customer customer) {
            claims.put("customerId", customer.getCustomerId());
            claims.put("name", customer.getName());
            claims.put("role", customer.getRole().name());
        }
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {

        Date now = new Date(System.currentTimeMillis());
        Date expiration = new Date(now.getTime() + jwtTokenValidity);

        return  Jwts.builder()
                .claims()
                .add(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiration)
                .and()
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String jwtToken) {
        return extractAllClaims(jwtToken).getSubject();
    }

    public String extractRole(String token) {
        return extractClaims(token, claims -> claims.get("role", String.class));
    }

    private <T> T extractClaims(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    public boolean validateToken(String jwtToken, UserDetails userDetails) {
        final String email = extractUserName(jwtToken);
        return (email.equals(userDetails.getUsername()) && isTokenExpired(jwtToken));
    }

    private boolean isTokenExpired(String jwtToken) {
        try {
            return !extractExpiration(jwtToken).before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaims(jwtToken, Claims::getExpiration);
    }

    public String extractFullName(String jwtToken) {
        return extractClaims(jwtToken, claims -> claims.get("name", String.class));
    }

    public Integer extractUserId(String token) {
        return extractClaims(token, claims -> claims.get("customerId", Integer.class));
    }
}

package com.ecommerce.service;

import org.springframework.stereotype.Service;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

/**
 * Ultra-simple password service using Base64 encoding
 * WARNING: This is NOT secure and should ONLY be used for development/learning
 * Base64 is encoding, not encryption - passwords can be easily decoded
 */
@Service
public class SimplePasswordService {
    
    /**
     * "Encrypts" password using Base64 encoding (NOT SECURE)
     */
    public String encryptPassword(String plainPassword) {
        String saltedPassword = plainPassword + "_simple_salt";
        return Base64.getEncoder().encodeToString(saltedPassword.getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * Verifies password by encoding input and comparing
     */
    public boolean verifyPassword(String plainPassword, String encodedPassword) {
        String encodedInput = encryptPassword(plainPassword);
        return encodedInput.equals(encodedPassword);
    }
    
    /**
     * Very basic password validation
     */
    public boolean isValidPassword(String password) {
        return password != null && password.length() >= 3;
    }
    
    public String getPasswordRequirements() {
        return "Password must be at least 3 characters long";
    }
    
    /**
     * Debug method to show what the "encrypted" password looks like
     * (You can remove this in production)
     */
    public String debugDecodePassword(String encodedPassword) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encodedPassword);
            return new String(decodedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "Invalid encoded password";
        }
    }
}
package com.ecommerce.service;

import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 * Simple password service using SHA-256 hashing
 * NOTE: This is for development/simple applications only
 */
@Service
public class PasswordService {
    
    private static final String SALT = "ecommerce_salt_2024"; // Simple salt
    
    /**
     * Encrypts a plain text password using SHA-256
     * @param plainPassword the plain text password
     * @return the encrypted password hash
     */
    public String encryptPassword(String plainPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String saltedPassword = plainPassword + SALT;
            byte[] hash = digest.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));
            
            // Convert byte array to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
    
    /**
     * Verifies if a plain text password matches the encrypted password
     * @param plainPassword the plain text password to verify
     * @param encryptedPassword the encrypted password from database
     * @return true if passwords match, false otherwise
     */
    public boolean verifyPassword(String plainPassword, String encryptedPassword) {
        String hashedInput = encryptPassword(plainPassword);
        return hashedInput.equals(encryptedPassword);
    }
    
    /**
     * Simple password validation - just length check
     * @param password the password to validate
     * @return true if password meets basic requirements
     */
    public boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
    
    /**
     * Gets password validation requirements as a string
     * @return string describing password requirements
     */
    public String getPasswordRequirements() {
        return "Password must be at least 6 characters long";
    }
}
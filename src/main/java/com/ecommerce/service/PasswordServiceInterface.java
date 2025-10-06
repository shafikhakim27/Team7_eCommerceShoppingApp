package com.ecommerce.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 * Interface for password management operations with default implementations
 * Defines contract for password encryption, verification, and validation
 */
public interface PasswordServiceInterface {
    
    /**
     * Encrypts a plain text password using SHA-256
     * @param plainPassword the plain text password
     * @return the encrypted password hash
     */
    default String encryptPassword(String plainPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
            
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
    default boolean verifyPassword(String plainPassword, String encryptedPassword) {
        String hashedInput = encryptPassword(plainPassword);
        return hashedInput.equals(encryptedPassword);
    }
    
    /**
     * Validates if a password meets the required criteria
     * @param password the password to validate
     * @return true if password meets requirements, false otherwise
     */
    default boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
    
    /**
     * Gets password validation requirements as a descriptive string
     * @return string describing password requirements
     */
    default String getPasswordRequirements() {
        return "Password must be at least 6 characters long";
    }
}
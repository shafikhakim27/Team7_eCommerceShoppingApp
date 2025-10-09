package com.ecommerce.service;

import com.ecommerce.model.User;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Service
public class PasswordServiceImpl implements PasswordServiceInterface {
    
    @Override
    public boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if ("!@#$%^&*(),.?\":{}|<>".indexOf(c) >= 0) {
                hasSpecial = true;
            }
        }
        
        return hasUppercase && hasLowercase && hasDigit && hasSpecial;
    }
    
    @Override
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        // Hash the plain password and compare with stored hash
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        String hashedPlainPassword = encryptPassword(plainPassword);
        return hashedPlainPassword.equals(hashedPassword);
    }
    
    @Override
    public String getPasswordRequirements() {
        return "Password must be at least 8 characters long and contain: " +
        "uppercase letter, lowercase letter, digit, and special character (!@#$%^&*(),.?\":{}|<>)";
    }
    
    @Override
    public String encryptPassword(String password) {
        try {
            // Use SHA-256 for password hashing
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error encrypting password", e);
        }
    }
    
    // Authentication operations (password-related)
    
    @Override
    public User registerUserWithPassword(User user, UserService userService) {
        // Validate password strength
        if (!isValidPassword(user.getPassword())) {
            throw new IllegalArgumentException(getPasswordRequirements());
        }
        
        // Encrypt password before saving
        String encryptedPassword = encryptPassword(user.getPassword());
        user.setPassword(encryptedPassword);
        
        // Use UserService for actual registration
        return userService.registerUser(user);
    }
    
    @Override
    public Optional<User> authenticateUser(String usernameOrEmail, String plainPassword, UserService userService) {
        return userService.findByUsernameOrEmail(usernameOrEmail)
                .filter(user -> verifyPassword(plainPassword, user.getPassword()));
    }
    
    @Override
    public User changeUserPassword(User user, String newPassword, UserService userService) {
        // Validate new password strength
        if (!isValidPassword(newPassword)) {
            throw new IllegalArgumentException(getPasswordRequirements());
        }
        
        // Encrypt new password
        String encryptedPassword = encryptPassword(newPassword);
        user.setPassword(encryptedPassword);
        
        // Save updated user
        userService.saveUser(user);
        return user;
    }
}
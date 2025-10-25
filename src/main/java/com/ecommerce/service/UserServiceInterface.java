package com.ecommerce.service;

import com.ecommerce.model.User;
import java.util.List;

/**
 * UserServiceInterface - Optimized interface for User operations
 * Lean baseline development with essential functionality
 */
public interface UserServiceInterface {
    
    // Core user operations
    User saveUser(User user);
    User registerUser(User user);
    User authenticateUser(String username, String password);
    User updateUser(User user);
    boolean deactivateUser(Long userId);
    
    // Finder methods
    User findById(Long id);
    User findByUsername(String username);
    User findByEmail(String email);
    User findByUsernameOrEmail(String usernameOrEmail);
    
    // Extended operations
    boolean changePassword(String email, String newPassword);
    List<User> getAllActiveUsers();
    long getActiveUserCount();
    
    // Relationship loading
    User getUserWithOrders(Long userId);
    User getUserWithCart(Long userId);
}


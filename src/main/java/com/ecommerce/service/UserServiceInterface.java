package com.ecommerce.service;

import com.ecommerce.model.User;
import java.util.List;
import java.util.Optional;

public interface UserServiceInterface {
    
    // Essential user lookup methods
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String usernameOrEmail);
    
    // User existence checks
    boolean existsByEmail(String email);
    
    // User management operations
    List<User> getAllUsers();
    User registerUser(User user); // Note: expects password to be already validated and encrypted
    void saveUser(User user);
}

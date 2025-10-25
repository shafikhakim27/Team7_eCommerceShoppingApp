package com.ecommerce.service;

import com.ecommerce.model.User;
import java.util.List;
import java.util.Optional;

/**
 * CustomerInterface for customer operations
 */
public interface CustomerInterface {
    
    User saveCustomer(User customer);
    
    Optional<User> findById(Long id);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUsername(String username);
    
    List<User> getAllCustomers();
    
    void deleteCustomer(Long id);
    
    boolean existsByEmail(String email);
    
    boolean existsByUsername(String username);
}
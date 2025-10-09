package com.ecommerce.service;

import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserServiceInterface {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    // Essential user lookup methods
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByEmailOrName(usernameOrEmail);
    }
    
    // User existence checks
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    // User management operations
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User registerUser(User user) {
        // Check email uniqueness (simplified - removed username check since we use email as primary identifier)
        if (existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Note: Password should be already validated and encrypted before calling this method
        return userRepository.save(user);
    }
    
    public void saveUser(User user) {
        userRepository.save(user);
    }
}


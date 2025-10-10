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
        // Check email uniqueness
        if (existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Password validation for development phase
        if (user.getPassword() == null || user.getPassword().length() < 3) {
            throw new IllegalArgumentException("Password must be at least 3 characters long");
        }
        
        // Save user with plain text password for development ease
        return userRepository.save(user);
    }
    
    // Simple authentication method for development phase
    public Optional<User> authenticateUser(String email, String password) {
        Optional<User> userOpt = findByEmail(email);
        
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt;
        }
        
        return Optional.empty();
    }
    
    // Method to change password (for development phase)
    public User changePassword(Long userId, String newPassword) {
        if (newPassword == null || newPassword.length() < 3) {
            throw new IllegalArgumentException("Password must be at least 3 characters long");
        }
        
        Optional<User> userOpt = findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        
        User user = userOpt.get();
        user.setPassword(newPassword);
        return userRepository.save(user);
    }
    
    public void saveUser(User user) {
        userRepository.save(user);
    }
}

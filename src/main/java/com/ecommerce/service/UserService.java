package com.ecommerce.service;

import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public UserService(UserRepository userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }
    
    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Validate password strength
        if (!passwordService.isValidPassword(user.getPassword())) {
            throw new IllegalArgumentException(passwordService.getPasswordRequirements());
        }
        
        // Encrypt password before saving
        String encryptedPassword = passwordService.encryptPassword(user.getPassword());
        user.setPassword(encryptedPassword);
        
        return userRepository.save(user);
    }
    
    /**
     * Authenticates user with username/email and password
     * @param usernameOrEmail username or email
     * @param plainPassword plain text password
     * @return authenticated user if credentials are valid
     */
    public Optional<User> authenticateUser(String usernameOrEmail, String plainPassword) {
        Optional<User> userOpt = userRepository.findByUsername(usernameOrEmail);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByEmail(usernameOrEmail);
        }
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordService.verifyPassword(plainPassword, user.getPassword())) {
                return Optional.of(user);
            }
        }
        
        return Optional.empty();
    }
    
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
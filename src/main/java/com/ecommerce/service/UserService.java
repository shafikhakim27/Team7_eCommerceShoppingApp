package com.ecommerce.service;

import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordServiceInterface passwordService;

    public UserService(UserRepository userRepository, PasswordServiceInterface passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }
    
    // Custom query methods (moved from repository)
    public Optional<User> findByUsername(String username) {
        return userRepository.findAll().stream()
            .filter(user -> user.getUsername().equals(username))
            .findFirst();
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findAll().stream()
            .filter(user -> user.getEmail().equals(email))
            .findFirst();
    }
    
    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }
    
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
    
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        // Check if it's an email (contains @) or username
        if (usernameOrEmail.contains("@")) {
            return findByEmail(usernameOrEmail);
        } else {
            return findByUsername(usernameOrEmail);
        }
    }
    
    public User registerUser(User user) {
        if (existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (existsByEmail(user.getEmail())) {
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
        Optional<User> userOpt = findByUsernameOrEmail(usernameOrEmail);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordService.verifyPassword(plainPassword, user.getPassword())) {
                return Optional.of(user);
            }
        }
        
        return Optional.empty();
    }
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
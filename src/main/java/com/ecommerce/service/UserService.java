package com.ecommerce.service;

import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * UserService - Optimized service for User operations
 * Lean baseline development with essential functionality
 */
@Service
@Transactional
public class UserService implements UserServiceInterface {
    
    private final UserRepository userRepository;
    // Note: Password encryption should be added for production
    // private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    // Core user operations
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User registerUser(User user) {
        // Validate input
        if (user.getUsername() == null || user.getUsername().trim().length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters long");
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new IllegalArgumentException("Valid email is required");
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }

        // Check for existing users
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username '" + user.getUsername() + "' already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email '" + user.getEmail() + "' already exists");
        }
        
        // Set default values
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User authenticateUser(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        
        // Try username first, then email
        User user = userRepository.findByUsernameAndPassword(username, password);
        if (user == null) {
            user = userRepository.findByEmailAndPassword(username, password);
        }
        
        return (user != null && user.getIsActive()) ? user : null;
    }
    
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
    
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    @Transactional(readOnly = true)
    public User findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail).orElse(null);
    }
    
    public boolean changePassword(String email, String newPassword) {
        if (newPassword == null || newPassword.trim().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        
        User user = findByEmail(email);
        if (user == null || !user.getIsActive()) {
            return false;
        }
        
        user.setPassword(newPassword.trim());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return true;
    }
    
    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID is required for update");
        }
        
        User existingUser = findById(user.getId());
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found");
        }
        
        // Update allowed fields
        if (user.getFirstName() != null) existingUser.setFirstName(user.getFirstName());
        if (user.getLastName() != null) existingUser.setLastName(user.getLastName());
        if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            existingUser.setEmail(user.getEmail());
        }
        
        existingUser.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(existingUser);
    }
    
    public boolean deactivateUser(Long userId) {
        User user = findById(userId);
        if (user == null) {
            return false;
        }
        
        user.setIsActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return true;
    }
    
    @Transactional(readOnly = true)
    public List<User> getAllActiveUsers() {
        return userRepository.findAllActiveUsers();
    }
    
    @Transactional(readOnly = true)
    public long getActiveUserCount() {
        return userRepository.countActiveUsers();
    }
    
    @Transactional(readOnly = true)
    public User getUserWithOrders(Long userId) {
        return userRepository.findByIdWithOrders(userId).orElse(null);
    }
    
    @Transactional(readOnly = true)
    public User getUserWithCart(Long userId) {
        return userRepository.findByIdWithCart(userId).orElse(null);
    }
}

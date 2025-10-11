package com.ecommerce.service;

import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserServiceInterface {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User registerUser(User user) {

        User existingUserByUsername = userRepository.findByUsername(user.getUsername());
        if (existingUserByUsername != null) {
            throw new IllegalArgumentException("Username '" + user.getUsername() + "' already exists");
        }
        
        User existingUserByEmail = userRepository.findByEmail(user.getEmail());
        if (existingUserByEmail != null) {
            throw new IllegalArgumentException("Email '" + user.getEmail() + "' already exists");
        }
        
        return userRepository.save(user);
    }

    public User authenticateUser(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public User findById(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.orElse(null);
    }
    
    public boolean changePassword(String email, String newPassword) {
        if (newPassword == null || newPassword.trim().length() < 3) {
            throw new IllegalArgumentException("Password must be at least 3 characters long");
        }
        
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false; // User not found
        }
        
        user.setPassword(newPassword.trim());
        userRepository.save(user);
        return true;
    }
}

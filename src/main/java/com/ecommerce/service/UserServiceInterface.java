package com.ecommerce.service;

import com.ecommerce.model.User;

public interface UserServiceInterface {
    User authenticateUser(String username, String password);
    User registerUser(User user);
    void saveUser(User user);
    User findById(Long id);
    User findByUsername(String username);
    User findByEmail(String email);
    boolean changePassword(String email, String newPassword);
}


package com.ecommerce.service;

import com.ecommerce.model.User;
import java.util.Optional;

public interface PasswordServiceInterface {

    boolean isValidPassword(String password);

    boolean verifyPassword(String plainPassword, String hashedPassword);

    String getPasswordRequirements();

    String encryptPassword(String password);
    
    // Authentication operations (password-related)
    
    User registerUserWithPassword(User user, UserService userService);
    
    Optional<User> authenticateUser(String usernameOrEmail, String plainPassword, UserService userService);
    
    User changeUserPassword(User user, String newPassword, UserService userService);

}

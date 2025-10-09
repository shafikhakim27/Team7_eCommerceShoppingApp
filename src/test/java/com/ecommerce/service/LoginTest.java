package com.ecommerce.service;

import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive Login Test Suite
 * Tests user registration, password validation, and authentication flows
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class LoginTest {

    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordServiceInterface passwordService;
    
    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private final String testEmail = "test@example.com";
    private final String testUsername = "testuser";
    private final String validPassword = "Password123!";
    private final String invalidPassword = "weak";

    @BeforeEach
    void setUp() {
        // Clean up any existing test data
        userRepository.deleteAll();
        
        // Create a test user
        testUser = new User();
        testUser.setName(testUsername);
        testUser.setEmail(testEmail);
        testUser.setPassword(validPassword);
    }

    @Test
    @DisplayName("Should validate strong passwords correctly")
    void testPasswordValidation() {
        // Test valid password
        assertTrue(passwordService.isValidPassword("Password123!"),
                "Should accept password with uppercase, lowercase, digit, and special char");
        
        // Test invalid passwords
        assertFalse(passwordService.isValidPassword("password"), "Should reject password without uppercase");
        assertFalse(passwordService.isValidPassword("PASSWORD"), "Should reject password without lowercase");
        assertFalse(passwordService.isValidPassword("Password"), "Should reject password without digit");
        assertFalse(passwordService.isValidPassword("Password123"), "Should reject password without special char");
        assertFalse(passwordService.isValidPassword("Pass1!"), "Should reject password too short");
        assertFalse(passwordService.isValidPassword(null), "Should reject null password");
        assertFalse(passwordService.isValidPassword(""), "Should reject empty password");
    }

    @Test
    @DisplayName("Should encrypt and verify passwords correctly")
    void testPasswordEncryptionAndVerification() {
        String originalPassword = "TestPassword123!";
        
        // Encrypt password
        String encryptedPassword = passwordService.encryptPassword(originalPassword);
        
        // Verify encrypted password is different from original
        assertThat(encryptedPassword).isNotEqualTo(originalPassword);
        assertThat(encryptedPassword).isNotNull();
        assertThat(encryptedPassword).isNotEmpty();
        
        // Verify password verification works
        assertTrue(passwordService.verifyPassword(originalPassword, encryptedPassword),
                "Should verify correct password");
        
        assertFalse(passwordService.verifyPassword("WrongPassword", encryptedPassword),
                "Should reject incorrect password");
        
        assertFalse(passwordService.verifyPassword(null, encryptedPassword),
                "Should reject null password");
        
        assertFalse(passwordService.verifyPassword(originalPassword, null),
                "Should reject null encrypted password");
    }

    @Test
    @DisplayName("Should register user with valid credentials")
    void testSuccessfulUserRegistration() {
        // Register user with password
        User registeredUser = passwordService.registerUserWithPassword(testUser, userService);
        
        // Verify user was registered
        assertThat(registeredUser).isNotNull();
        assertThat(registeredUser.getId()).isNotNull();
        assertThat(registeredUser.getName()).isEqualTo(testUsername);
        assertThat(registeredUser.getEmail()).isEqualTo(testEmail);
        
        // Verify password was encrypted
        assertThat(registeredUser.getPassword()).isNotEqualTo(validPassword);
        
        // Verify user exists in database
        Optional<User> foundUser = userService.findByEmail(testEmail);
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo(testUsername);
    }

    @Test
    @DisplayName("Should reject registration with weak password")
    void testRegistrationWithWeakPassword() {
        testUser.setPassword(invalidPassword);
        
        // Should throw exception for weak password
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> passwordService.registerUserWithPassword(testUser, userService),
                "Should reject weak password"
        );
        
        assertThat(exception.getMessage()).contains("Password must be at least 8 characters");
    }

    @Test
    @DisplayName("Should reject registration with duplicate email")
    void testRegistrationWithDuplicateEmail() {
        // Register first user
        passwordService.registerUserWithPassword(testUser, userService);
        
        // Try to register another user with same email
        User duplicateUser = new User();
        duplicateUser.setName("anotheruser");
        duplicateUser.setEmail(testEmail); // Same email
        duplicateUser.setPassword(validPassword);
        
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> passwordService.registerUserWithPassword(duplicateUser, userService),
                "Should reject duplicate email"
        );
        
        assertThat(exception.getMessage()).contains("Email already exists");
    }

    @Test
    @DisplayName("Should authenticate user with correct credentials")
    void testSuccessfulAuthentication() {
        // Register user first
        passwordService.registerUserWithPassword(testUser, userService);
        
        // Test authentication with email
        Optional<User> authenticatedUser = passwordService.authenticateUser(testEmail, validPassword, userService);
        
        assertThat(authenticatedUser).isPresent();
        assertThat(authenticatedUser.get().getEmail()).isEqualTo(testEmail);
        assertThat(authenticatedUser.get().getName()).isEqualTo(testUsername);
        
        // Test authentication with username
        Optional<User> authenticatedByUsername = passwordService.authenticateUser(testUsername, validPassword, userService);
        
        assertThat(authenticatedByUsername).isPresent();
        assertThat(authenticatedByUsername.get().getEmail()).isEqualTo(testEmail);
    }

    @Test
    @DisplayName("Should reject authentication with incorrect credentials")
    void testFailedAuthentication() {
        // Register user first
        passwordService.registerUserWithPassword(testUser, userService);
        
        // Test with wrong password
        Optional<User> result1 = passwordService.authenticateUser(testEmail, "WrongPassword", userService);
        assertThat(result1).isEmpty();
        
        // Test with wrong email
        Optional<User> result2 = passwordService.authenticateUser("wrong@example.com", validPassword, userService);
        assertThat(result2).isEmpty();
        
        // Test with null credentials
        Optional<User> result3 = passwordService.authenticateUser(null, validPassword, userService);
        assertThat(result3).isEmpty();
        
        Optional<User> result4 = passwordService.authenticateUser(testEmail, null, userService);
        assertThat(result4).isEmpty();
    }

    @Test
    @DisplayName("Should change user password successfully")
    void testPasswordChange() {
        // Register user first
        User registeredUser = passwordService.registerUserWithPassword(testUser, userService);
        String oldEncryptedPassword = registeredUser.getPassword();
        
        String newPassword = "NewPassword456!";
        
        // Change password
        User updatedUser = passwordService.changeUserPassword(registeredUser, newPassword, userService);
        
        // Verify password was changed
        assertThat(updatedUser.getPassword()).isNotEqualTo(oldEncryptedPassword);
        assertThat(updatedUser.getPassword()).isNotEqualTo(newPassword); // Should be encrypted
        
        // Verify old password no longer works
        Optional<User> authWithOldPw = passwordService.authenticateUser(testEmail, validPassword, userService);
        assertThat(authWithOldPw).isEmpty();
        
        // Verify new password works
        Optional<User> authWithNewPw = passwordService.authenticateUser(testEmail, newPassword, userService);
        assertThat(authWithNewPw).isPresent();
    }

    @Test
    @DisplayName("Should find users by different identifiers")
    void testUserLookupMethods() {
        // Register user first
        User registeredUser = passwordService.registerUserWithPassword(testUser, userService);
        Long userId = registeredUser.getId();
        
        // Test find by ID
        Optional<User> foundById = userService.findById(userId);
        assertThat(foundById).isPresent();
        assertThat(foundById.get().getEmail()).isEqualTo(testEmail);
        
        // Test find by email
        Optional<User> foundByEmail = userService.findByEmail(testEmail);
        assertThat(foundByEmail).isPresent();
        assertThat(foundByEmail.get().getName()).isEqualTo(testUsername);
        
        // Test find by username or email
        Optional<User> foundByUsername = userService.findByUsernameOrEmail(testUsername);
        assertThat(foundByUsername).isPresent();
        
        Optional<User> foundByEmailAgain = userService.findByUsernameOrEmail(testEmail);
        assertThat(foundByEmailAgain).isPresent();
        
        // Test existence checks
        assertTrue(userService.existsByEmail(testEmail));
        assertFalse(userService.existsByEmail("nonexistent@example.com"));
    }

    @Test
    @DisplayName("Should get password requirements")
    void testPasswordRequirements() {
        String requirements = passwordService.getPasswordRequirements();
        
        assertThat(requirements).isNotNull();
        assertThat(requirements).isNotEmpty();
        assertThat(requirements).contains("8 characters");
        assertThat(requirements).contains("uppercase");
        assertThat(requirements).contains("lowercase");
        assertThat(requirements).contains("digit");
        assertThat(requirements).contains("special character");
    }

    @Test
    @DisplayName("Should handle edge cases gracefully")
    void testEdgeCases() {
        // Test with edge case inputs
        assertFalse(passwordService.isValidPassword("1234567!")); // No uppercase
        assertFalse(passwordService.isValidPassword("ABCDEFG!")); // No lowercase
        assertFalse(passwordService.isValidPassword("Abcdefgh")); // No digit or special
        
        // Test encryption with edge cases
        assertThrows(RuntimeException.class, () -> {
            // This should not happen in normal operation, but test error handling
            passwordService.verifyPassword("test", "");
        });
    }
}
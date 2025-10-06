package com.ecommerce.controller;

import com.ecommerce.model.User;
import com.ecommerce.service.UserService;
import com.ecommerce.service.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for login functionality
 * Tests the AuthController login endpoints and authentication flow
 */
@WebMvcTest(AuthController.class)
public class LoginTest {

@Autowired
private MockMvc mockMvc;
private UserService userService;
private User testUser;

@BeforeEach
void setUp() {
        // Create test user
        testUser = new User();
        testUser.setUserID(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("hashedpassword123");
}

@Test
void testLoginPageDisplays() throws Exception {
        // Test that login page loads correctly
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
}

@Test
void testSuccessfulLogin() throws Exception {
        // Mock successful authentication
        when(userService.authenticateUser("testuser", "password123"))
                .thenReturn(Optional.of(testUser));

        // Test successful login
        mockMvc.perform(post("/login")
                .param("usernameOrEmail", "testuser")
                .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
}

@Test
void testFailedLoginWithWrongPassword() throws Exception {
        // Mock failed authentication
        when(userService.authenticateUser("testuser", "wrongpassword"))
                .thenReturn(Optional.empty());

        // Test failed login
        mockMvc.perform(post("/login")
                .param("usernameOrEmail", "testuser")
                .param("password", "wrongpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("error", "Invalid username/email or password"));
}

@Test
void testFailedLoginWithNonExistentUser() throws Exception {
        // Mock failed authentication for non-existent user
        when(userService.authenticateUser("nonexistent", "password123"))
                .thenReturn(Optional.empty());

        // Test failed login
        mockMvc.perform(post("/login")
                .param("usernameOrEmail", "nonexistent")
                .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("error", "Invalid username/email or password"));
}

@Test
void testLoginWithEmptyCredentials() throws Exception {
        // Test login with empty credentials
        mockMvc.perform(post("/login")
                .param("usernameOrEmail", "")
                .param("password", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("error", "Username/email and password are required"));
}

@Test
void testLoginWithEmailInsteadOfUsername() throws Exception {
        // Mock successful authentication with email
        when(userService.authenticateUser("test@example.com", "password123"))
                .thenReturn(Optional.of(testUser));

        // Test successful login with email
        mockMvc.perform(post("/login")
                .param("usernameOrEmail", "test@example.com")
                .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
}

@Test
void testRegisterPageDisplays() throws Exception {
        // Test that register page loads correctly
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
}

@Test
void testSuccessfulRegistration() throws Exception {
        // Mock successful user creation
        when(userService.existsByUsername("newuser")).thenReturn(false);
        when(userService.existsByEmail("new@example.com")).thenReturn(false);
        when(userService.registerUser(any(User.class))).thenReturn(testUser);

        // Test successful registration
        mockMvc.perform(post("/register")
                .param("username", "newuser")
                .param("email", "new@example.com")
                .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("success", "Registration successful! Please login."));
}

@Test
void testRegistrationWithExistingUsername() throws Exception {
        // Mock existing username
        when(userService.existsByUsername("existinguser")).thenReturn(true);

        // Test registration with existing username
        mockMvc.perform(post("/register")
                .param("username", "existinguser")
                .param("email", "new@example.com")
                .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attribute("error", "Username already exists"));
}

@Test
void testRegistrationWithExistingEmail() throws Exception {
        // Mock existing email
        when(userService.existsByUsername("newuser")).thenReturn(false);
        when(userService.existsByEmail("existing@example.com")).thenReturn(true);

        // Test registration with existing email
        mockMvc.perform(post("/register")
                .param("username", "newuser")
                .param("email", "existing@example.com")
                .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attribute("error", "Email already exists"));
}

@Test
void testRegistrationWithInvalidPassword() throws Exception {
        // Mock password validation failure
        when(userService.existsByUsername("newuser")).thenReturn(false);
        when(userService.existsByEmail("new@example.com")).thenReturn(false);
        when(userService.registerUser(any(User.class)))
                .thenThrow(new IllegalArgumentException("Password must be at least 6 characters long"));

        // Test registration with invalid password
        mockMvc.perform(post("/register")
                .param("username", "newuser")
                .param("email", "new@example.com")
                .param("password", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attribute("error", "Password must be at least 6 characters long"));
}

@Test
void testLogout() throws Exception {
        // Test logout functionality
        mockMvc.perform(post("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("success", "You have been logged out successfully"));
        }
}
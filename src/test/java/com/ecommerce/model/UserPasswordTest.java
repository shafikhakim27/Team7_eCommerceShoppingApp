package com.ecommerce.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for User model basic functionality
 * Tests user creation and basic getter/setter operations
 */
public class UserPasswordTest {

    @Test
    void testUserCreation() {
        // Test that user can be created with basic properties
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("testpassword123");
        
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("testpassword123", user.getPassword());
    }

    @Test
    void testUserIdGeneration() {
        // Test user ID functionality
        User user = new User();
        user.setUserID(1L);
        
        assertEquals(1L, user.getUserID());
    }

    @Test
    void testUserEquality() {
        // Test basic user properties
        User user1 = new User();
        user1.setUsername("testuser");
        user1.setEmail("test@example.com");
        user1.setPassword("password123");
        
        User user2 = new User();
        user2.setUsername("testuser");
        user2.setEmail("test@example.com");
        user2.setPassword("password123");
        
        // Test that users with same properties have same values
        assertEquals(user1.getUsername(), user2.getUsername());
        assertEquals(user1.getEmail(), user2.getEmail());
        assertEquals(user1.getPassword(), user2.getPassword());
    }

    @Test
    void testUserValidation() {
        // Test that user can store valid data
        User user = new User();
        
        // Test username
        user.setUsername("validusername");
        assertNotNull(user.getUsername());
        assertTrue(user.getUsername().length() >= 3);
        
        // Test email
        user.setEmail("valid@example.com");
        assertNotNull(user.getEmail());
        assertTrue(user.getEmail().contains("@"));
        
        // Test password
        user.setPassword("validpassword123");
        assertNotNull(user.getPassword());
        assertTrue(user.getPassword().length() >= 6);
    }

    @Test
    void testUserPasswordStorage() {
        // Test that password is stored correctly
        User user = new User();
        String originalPassword = "mypassword123";
        
        user.setPassword(originalPassword);
        
        assertEquals(originalPassword, user.getPassword());
        assertNotNull(user.getPassword());
        assertTrue(user.getPassword().length() > 0);
    }
}
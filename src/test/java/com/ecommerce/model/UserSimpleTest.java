package com.ecommerce.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class UserSimpleTest {
    
    private User user;
    
    @BeforeEach
    void setUp() {
        user = new User("testuser", "test@example.com", "password123");
    }
    
    @Test
    void testUserCreation() {
        assertNotNull(user);
        assertEquals("testuser", user.getName());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
    }
    
    @Test
    void testPasswordStorage() {
        // Test that password is stored directly (for development phase)
        String originalPassword = "simplepass";
        user.setPassword(originalPassword);
        assertEquals(originalPassword, user.getPassword());
    }
    
    @Test
    void testEqualsAndHashCode() {
        User user1 = new User("user1", "user1@example.com", "pass1");
        User user2 = new User("user2", "user2@example.com", "pass2");
        
        // Without IDs, they should not be equal
        assertNotEquals(user1, user2);
        
        // Set same ID
        user1.setId(1L);
        user2.setId(1L);
        
        // With same ID, they should be equal
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }
    
    @Test
    void testToString() {
        user.setId(1L);
        String toString = user.toString();
        
        // Should contain ID, name, email
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("name='testuser'"));
        assertTrue(toString.contains("email='test@example.com'"));
        
        // Should NOT contain password for security
        assertFalse(toString.contains("password"));
    }
}
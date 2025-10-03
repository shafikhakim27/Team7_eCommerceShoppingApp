package com.ecommerce.service;

import org.springframework.stereotype.Component;
import com.ecommerce.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

// Custom session management
@Component
public class SessionManager {
    
    public void createUserSession(User user, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("currentUser", user);
    }
    
    public User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null ? (User) session.getAttribute("currentUser") : null;
    }
}
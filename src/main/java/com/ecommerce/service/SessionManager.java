package com.ecommerce.service;

import org.springframework.stereotype.Component;
import com.ecommerce.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

// Custom session management
@Component
public class SessionManager {
    
    // login
    public void login(User user, HttpServletRequest request) {
        createUserSession(user, request);
    }
    // find user by session
    public User findUserBySession(HttpServletRequest request) {
        return getCurrentUser(request);
    }
    // find user by email
    public User findUserByEmail(String email, HttpServletRequest request) {
        User user = getCurrentUser(request);
        return (user != null && user.getEmail().equals(email)) ? user : null;
    }
    // create session
    public void createUserSession(User user, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("currentUser", user);
    }
    // get current user from session
    public User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null ? (User) session.getAttribute("currentUser") : null;
    }
    // logout
    public void logout(HttpServletRequest request) {
        invalidateSession(request);
    }
    // Invalidate session on logout
    public void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
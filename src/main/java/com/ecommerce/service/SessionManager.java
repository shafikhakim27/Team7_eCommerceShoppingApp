package com.ecommerce.service;

import org.springframework.stereotype.Component;
import com.ecommerce.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public class SessionManager {
    
    private static final String USER_SESSION_KEY = "currentUser";

    // login and create user session
    public void login(User user, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute(USER_SESSION_KEY, user);
    }
    
    // get current user
    public User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null ? (User) session.getAttribute(USER_SESSION_KEY) : null;
    }

    // check if user is logged in
    public boolean isLoggedIn(HttpServletRequest request) {
        return getCurrentUser(request) != null;
    }
    
    // logout and invalidate session
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}

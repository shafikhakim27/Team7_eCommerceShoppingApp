package com.ecommerce.controller;

import com.ecommerce.model.User;
import com.ecommerce.service.UserService;
import com.ecommerce.service.SessionManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

@Controller
public class AuthController {
    
    private final UserService userService;
    private final SessionManager sessionManager;
    
    public AuthController(UserService userService, SessionManager sessionManager) {
        this.userService = userService;
        this.sessionManager = sessionManager;
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @PostMapping("/login")
    public String processLogin(@RequestParam String usernameOrEmail,
            @RequestParam String password,
            Model model, HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        
        // Simple validation
        if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty()) {
            model.addAttribute("error", "Username or email is required");
            return "login";
        }
        
        if (password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "Password is required");
            return "login";
        }
        
        Optional<User> userOpt = userService.authenticateUser(
            usernameOrEmail.trim(),
            password
        );
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            sessionManager.login(user, request);
            redirectAttributes.addFlashAttribute("success", "Welcome back, " + user.getUsername() + "!");
            return "redirect:/dashboard"; // Redirect to dashboard or home page
        } else {
            model.addAttribute("error", "Invalid username/email or password");
            return "login";
        }
    }
    
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
    
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            Model model, RedirectAttributes redirectAttributes) {
        
        // Simple validation
        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("error", "Username is required");
            return "register";
        }
        
        if (email == null || email.trim().isEmpty()) {
            model.addAttribute("error", "Email is required");
            return "register";
        }
        
        if (password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "Password is required");
            return "register";
        }
        
        try {
            // Create User object from form parameters
            User user = new User();
            user.setUsername(username.trim());
            user.setEmail(email.trim());
            user.setPassword(password);
            
            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please log in.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
    // Additional methods for logout and session management can be added here
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        sessionManager.logout(request);
        redirectAttributes.addFlashAttribute("success", "You have been logged out successfully");
        return "redirect:/login";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest request, Model model) {
        User currentUser = sessionManager.getCurrentUser(request);
        if (currentUser == null) {
        return "redirect:/login";
        }
        model.addAttribute("user", currentUser);
        return "dashboard";
    }
    //
}
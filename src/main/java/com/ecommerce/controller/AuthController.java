package com.ecommerce.controller;

import com.ecommerce.dto.LoginRequest;
import com.ecommerce.model.User;
import com.ecommerce.service.UserService;
import com.ecommerce.service.SessionManager;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }
    
    @PostMapping("/login")
    public String processLogin(@Valid LoginRequest loginRequest, BindingResult result, 
                              Model model, HttpServletRequest request, 
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "login";
        }
        
        Optional<User> userOpt = userService.authenticateUser(
            loginRequest.getUsernameOrEmail(), 
            loginRequest.getPassword()
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
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    
    @PostMapping("/register")
    public String registerUser(@Valid User user, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "register";
        }
        
        try {
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
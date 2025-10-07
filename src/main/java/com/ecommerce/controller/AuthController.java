package com.ecommerce.controller;

import com.ecommerce.model.User;
import com.ecommerce.service.UserService;
import com.ecommerce.service.PasswordServiceInterface;
import com.ecommerce.service.SessionManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordServiceInterface passwordService;
    private final SessionManager sessionManager;

    public AuthController(UserService userService, PasswordServiceInterface passwordService, SessionManager sessionManager) {
        this.userService = userService;
        this.passwordService = passwordService;
        this.sessionManager = sessionManager;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String email,
                                @RequestParam String password,
                                HttpServletRequest request,
                            RedirectAttributes redirectAttributes) {
        Optional<User> userOpt = passwordService.authenticateUser(email, password, userService);
        
        if (userOpt.isPresent()) {
            sessionManager.login(userOpt.get(), request);
            return "redirect:/dashboard";
        }
        
        redirectAttributes.addFlashAttribute("error", "Invalid email or password.");
        return "redirect:/auth/login";
    }

    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register";
    }

    @PostMapping("/register")
    public String handleRegistration(@RequestParam String name,
                                        @RequestParam String email,
                                        @RequestParam String password,
                                        RedirectAttributes redirectAttributes) {
        try {
            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPassword(password);
            
            passwordService.registerUserWithPassword(newUser, userService);
            redirectAttributes.addFlashAttribute("success", "Registration successful. Please log in.");
            return "redirect:/auth/login";
            
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/register";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpServletRequest request) {
        sessionManager.logout(request);
        return "redirect:/auth/login?logout";
    }
}
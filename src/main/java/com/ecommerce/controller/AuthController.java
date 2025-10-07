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
    public String showLoginPage() {
        return "login"; // Return the login view
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String email, @RequestParam String password, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Optional<User> userOpt = userService.authenticate(email, password);
        if (userOpt.isPresent()) {
            // Successful login
            sessionManager.createSession(request, userOpt.get());
            return "redirect:/dashboard"; // Redirect to a dashboard or home page
        } else {
            // Failed login
            redirectAttributes.addFlashAttribute("error", "Invalid email or password.");
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register"; // Return the registration view
    }

    @PostMapping("/register")
    public String handleRegistration(@RequestParam String name, @RequestParam String email, @RequestParam String password, RedirectAttributes redirectAttributes) {
        if (userService.emailExists(email)) {
            redirectAttributes.addFlashAttribute("error", "Email already in use.");
            return "redirect:/register";
        }

        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(password); // In a real app, ensure to hash the password

        userService.saveUser(newUser);
        redirectAttributes.addFlashAttribute("success", "Registration successful. Please log in.");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String handleLogout(HttpServletRequest request) {
        sessionManager.invalidateSession(request);
        return "redirect:/login?logout"; // Redirect to login page with logout message
    }
}
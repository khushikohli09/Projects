package com.univ.attendograde2v.auth.controller;

import com.univ.attendograde2v.auth.entity.User;
import com.univ.attendograde2v.auth.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {

        User user = authService.authenticate(username, password);

        if (user != null) {
            // Save user details in session
            session.setAttribute("username", user.getUsername());
            session.setAttribute("email", user.getEmail());
            session.setAttribute("role", user.getRole());

            // Redirect based on role
            if (user.getRole().equalsIgnoreCase("STUDENT")) {
                return "redirect:/student/home";
            } else if (user.getRole().equalsIgnoreCase("FACULTY")) {
                return "redirect:/faculty/home";
            } else {
                return "redirect:/auth/login";
            }
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    // Register page
    @GetMapping("/register")
    public String registerPage() {
        return "register"; // this should map to register.html (Thymeleaf/JSP)
    }

    // Handle registration
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String role,
                               Model model) {

        User user = authService.registerUser(username, email, password, role);

        if (user != null) {
            // Auto-login after registration (optional)
            model.addAttribute("success", "Registration successful! Please login.");
            return "redirect:/auth/login";
        } else {
            model.addAttribute("error", "Registration failed. Try again.");
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}

package com.univ.attendograde2v.auth.service;

import com.univ.attendograde2v.auth.entity.User;
import com.univ.attendograde2v.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register a new user
    public User registerUser(String username, String email, String password, String role) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        // Default role is STUDENT if not provided
        if (role == null || role.isEmpty()) {
            role = "STUDENT";
        }
        user.setRole(role.toUpperCase());

        return userRepository.save(user);
    }

    // Check if username already exists
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    // Check if email already exists
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // Fetch user by username
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    // Authenticate user during login
    public User authenticate(String username, String rawPassword) {
        User user = getUserByUsername(username);
        if (user != null && passwordEncoder.matches(rawPassword, user.getPassword())) {
            return user; // Authentication successful
        }
        return null; // Invalid credentials
    }
}

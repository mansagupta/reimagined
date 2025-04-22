package com.example.safetyAlert.service;

import com.example.safetyAlert.model.User;
import com.example.safetyAlert.model.AuthRequest;
import com.example.safetyAlert.model.AuthResponse;
import com.example.safetyAlert.repository.UserRepository;
import com.example.safetyAlert.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserService userService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService,
                       UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.userService = userService;
    }

    public ResponseEntity<Map<String, String>> register(User user) {
        if (userRepository.findByMobile(user.getMobile()).isPresent()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Username is already in use.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully!");
        response.put("username", user.getUsername());

        return ResponseEntity.ok(response);
    }

    public AuthResponse login(AuthRequest authRequest) {
        try {
            System.out.println("Attempting authentication for: " + authRequest.getMobile());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getMobile(), authRequest.getPassword())
            );

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(authRequest.getMobile());
            String token = jwtUtil.generateToken(userDetails);
            String mobile = authRequest.getMobile();

            System.out.println("Authentication successful! Token: " + token);
            return new AuthResponse(token, mobile);
        } catch (Exception e) {
            System.out.println("Authentication failed: " + e.getMessage());
            throw new RuntimeException("Invalid login credentials", e);
        }
    }
    public ResponseEntity<String> getUsernameFromMobile(String mobile) {
        String username = userService.getUsernameByMobile(mobile);
        return ResponseEntity.ok(username);
    }

    public ResponseEntity<Map<String, String>> resetPassword(String username, String newPassword) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "User not found"));
        }

        User existingUser = optionalUser.get();

        User updatedUser = new User();
        updatedUser.setUsername(existingUser.getUsername());
        updatedUser.setEmail(existingUser.getEmail());
        updatedUser.setMobile(existingUser.getMobile());

        updatedUser.setPassword(passwordEncoder.encode(newPassword));

        userRepository.deleteByUsername(existingUser);
        System.out.println("user deleted successfully.");

        userRepository.save(updatedUser);

        return ResponseEntity.ok(Collections.singletonMap("message", "Password updated successfully"));
    }

}

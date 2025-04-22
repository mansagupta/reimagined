package com.example.safetyAlert.controller;

import com.example.safetyAlert.model.AuthRequest;
import com.example.safetyAlert.model.AuthResponse;
import com.example.safetyAlert.model.ResetPasswordRequest;
import com.example.safetyAlert.model.User;
import com.example.safetyAlert.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseEntity<Map<String, String>>> register(@RequestBody User user) {
        return ResponseEntity.ok(authService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.login(authRequest));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody ResetPasswordRequest request) {
        return authService.resetPassword(request.getUsername(), request.getNewPassword());
    }
    @PostMapping("/username")
    public ResponseEntity<?> getUserName(@RequestBody String Mobile) {
        return authService.getUsernameFromMobile(Mobile);
    }
}

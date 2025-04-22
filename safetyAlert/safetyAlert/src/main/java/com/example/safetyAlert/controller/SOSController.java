package com.example.safetyAlert.controller;

import com.example.safetyAlert.model.SOSRequest;
import com.example.safetyAlert.service.SOSService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sos")
public class SOSController {

    private final SOSService sosService;

    public SOSController(SOSService sosService) {
        this.sosService = sosService;
    }

    @PostMapping("/trigger")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SOSRequest> triggerSOS(@RequestBody Map<String, Object> payload) {
        String username = (String) payload.get("username");
        double latitude = (double) payload.get("latitude");
        double longitude = (double) payload.get("longitude");

        SOSRequest sosRequest = sosService.createSOSRequest(username, latitude, longitude);
        return ResponseEntity.ok(sosRequest);
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<SOSRequest>> getUserSOSRequests(@PathVariable String username) {
        return ResponseEntity.ok(sosService.getUserSOSRequests(username));
    }

    @PutMapping("/resolve/{sosId}")
    public ResponseEntity<String> resolveSOS(@PathVariable String sosId) {
        sosService.resolveSOS(sosId);
        return ResponseEntity.ok("SOS Resolved Successfully");
    }
}

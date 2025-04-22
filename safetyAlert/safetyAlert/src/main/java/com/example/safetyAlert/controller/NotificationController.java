package com.example.safetyAlert.controller;

import com.example.safetyAlert.service.NotificationService;
import com.example.safetyAlert.service.FCMTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final FCMTokenService fcmTokenService;

    public NotificationController(NotificationService notificationService, FCMTokenService fcmTokenService) {
        this.notificationService = notificationService;
        this.fcmTokenService = fcmTokenService;
    }

    @PostMapping("/register-token")
    public ResponseEntity<String> registerToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String mobile = request.get("mobile");
        fcmTokenService.saveToken(mobile, token);
        return ResponseEntity.ok("FCM Token Registered Successfully");
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody Map<String, String> request) {
        String mobile = request.get("mobile");
        String title = request.get("title");
        String body = request.get("body");

        String token = fcmTokenService.getToken(mobile);
        if (token != null) {
            String response = notificationService.sendPushNotification(token, title, body);
            return ResponseEntity.ok("Notification Sent! Response: " + response);
        } else {
            return ResponseEntity.badRequest().body("User Token Not Found");
        }
    }
}

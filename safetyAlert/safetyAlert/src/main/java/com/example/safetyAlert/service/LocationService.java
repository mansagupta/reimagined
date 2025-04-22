package com.example.safetyAlert.service;

import com.example.safetyAlert.fcm.FirebaseMessagingService;
import com.example.safetyAlert.model.LocationUpdate;
import com.example.safetyAlert.repository.LocationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final FirebaseMessagingService firebaseMessagingService;
    private final FCMTokenService fcmTokenService;
    private final NotificationService notificationService;

    public LocationService(LocationRepository locationRepository, SimpMessagingTemplate messagingTemplate,
                           FirebaseMessagingService firebaseMessagingService, FCMTokenService fcmTokenService,
                           NotificationService notificationService) {
        this.locationRepository = locationRepository;
        this.messagingTemplate = messagingTemplate;
        this.firebaseMessagingService = firebaseMessagingService;
        this.fcmTokenService = fcmTokenService;
        this.notificationService = notificationService;
    }

    public List<LocationUpdate> getUserLocations(String username) {
        return locationRepository.findByUsernameOrderByTimestampDesc(username);
    }

    public LocationUpdate saveLocation(String username, double latitude, double longitude) {
        LocationUpdate locationUpdate = new LocationUpdate(username, latitude, longitude, LocalDateTime.now());
        LocationUpdate savedLocation = locationRepository.save(locationUpdate);

        // Send real-time WebSocket update
        messagingTemplate.convertAndSend("/topic/location/" + username, savedLocation);

        // Send push notification
        String token = fcmTokenService.getToken(username);
        if (token != null) {
            notificationService.sendPushNotification(token, "Location Update", "Your location was updated."); // ✅ Use instance method
        }

        return savedLocation;
    }
}

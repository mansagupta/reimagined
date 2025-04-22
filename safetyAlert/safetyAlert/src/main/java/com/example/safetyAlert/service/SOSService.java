package com.example.safetyAlert.service;

import com.example.safetyAlert.model.EmergencyContact;
import com.example.safetyAlert.model.SOSRequest;
import com.example.safetyAlert.repository.SOSRepository;
import com.example.safetyAlert.repository.EmergencyContactSearchRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SOSService {

    private final SOSRepository sosRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;
    private final EmergencyContactSearchRepository emergencyContactRepository;
    private final FCMTokenService fcmTokenService;

    public SOSService(SOSRepository sosRepository, SimpMessagingTemplate messagingTemplate,
                      NotificationService notificationService, EmergencyContactSearchRepository emergencyContactRepository,
                      FCMTokenService fcmTokenService) {
        this.sosRepository = sosRepository;
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
        this.emergencyContactRepository = emergencyContactRepository;
        this.fcmTokenService = fcmTokenService;
    }

    public SOSRequest createSOSRequest(String username, double latitude, double longitude) {
        SOSRequest sosRequest = new SOSRequest(username, latitude, longitude);
        sosRepository.save(sosRequest);

        // Fetch emergency contacts from the database
        System.out.println(("Creating SOS request."));
        Optional<EmergencyContact> emergencyContactOpt = emergencyContactRepository.findByUsername(username);
        System.out.println("Emergency contacts for user " + username + ": " + emergencyContactOpt);

        List<String> emergencyContacts = emergencyContactOpt
                .map(emergencyContact -> emergencyContact.getContacts().stream()
                        .map(EmergencyContact.Contact::getPhone)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        for (String contactUserId : emergencyContacts) {
            String contactToken = fcmTokenService.getToken(contactUserId);
            System.out.println("FCM Token for contact " + contactUserId + ": " + contactToken);

            if (contactToken != null) {
                notificationService.sendPushNotification(contactToken,
                        "Emergency Alert!",
                        "SOS triggered by user: " + username + "\nLocation: (" + latitude + ", " + longitude + ")");
            }else{
                System.out.println("No FCM token found for contact: " + contactUserId);
            }
        }


        // Send WebSocket update
        System.out.println("Sending SOS request.");
        messagingTemplate.convertAndSend("/topic/sos-alerts", sosRequest);

        return sosRequest;
    }

    public List<SOSRequest> getUserSOSRequests(String userId) {
        return sosRepository.findByUsername(userId);
    }

    public void resolveSOS(String sosId) {
        sosRepository.findById(sosId).ifPresent(sos -> {
            sos.setStatus("RESOLVED");
            sosRepository.save(sos);
            messagingTemplate.convertAndSend("/topic/sos-alerts", sos);
        });
    }
}

package com.example.safetyAlert.service;

import com.google.firebase.messaging.*;
import com.example.safetyAlert.fcm.FirebaseMessagingService;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final FirebaseMessaging firebaseMessaging;

    public NotificationService(FirebaseMessagingService firebaseMessagingService) {
        this.firebaseMessaging = firebaseMessagingService.getFirebaseMessaging();
    }

    public String sendPushNotification(String token, String title, String message) {
        try {
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(message)
                    .build();

            Message firebaseMessage = Message.builder()
                    .setNotification(notification)
                    .setToken(token) // Send to a specific device
                    .build();

            return firebaseMessaging.send(firebaseMessage);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return "Error sending push notification";
        }
    }

    public String sendPushNotificationToTopic(String topic, String title, String message) {
        try {
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(message)
                    .build();

            Message firebaseMessage = Message.builder()
                    .setNotification(notification)
                    .setTopic(topic) // Send to all devices subscribed to this topic
                    .build();

            return firebaseMessaging.send(firebaseMessage);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return "Error sending notification to topic";
        }
    }
}

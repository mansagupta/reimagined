package com.example.safetyAlert.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class FirebaseMessagingService {

    private FirebaseMessaging firebaseMessaging;

    @PostConstruct
    public void initFirebase() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(
                                new ClassPathResource("serviceAccountKey.json").getInputStream()))
                        .build();

                FirebaseApp.initializeApp(options);
            }

            this.firebaseMessaging = FirebaseMessaging.getInstance();
            System.out.println(" Firebase Messaging Service Initialized!");
        } catch (IOException e) {
            throw new RuntimeException(" Failed to initialize Firebase", e);
        }
    }

    public FirebaseMessaging getFirebaseMessaging() {
        return firebaseMessaging;
    }
}



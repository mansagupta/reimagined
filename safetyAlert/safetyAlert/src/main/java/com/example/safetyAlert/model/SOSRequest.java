package com.example.safetyAlert.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "sos_requests")
public class SOSRequest {

    @Id
    private String id;
    private String username;
    private double latitude;
    private double longitude;
    private LocalDateTime timestamp;
    private String status; // "ACTIVE" or "RESOLVED"

    public SOSRequest(String username, double latitude, double longitude) {
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = LocalDateTime.now();
        this.status = "ACTIVE";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
// Getters and Setters
}

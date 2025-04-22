package com.example.safetyAlert.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "location_updates")
public class LocationUpdate {
    @Id
    private String id;
    private String username;
    private double latitude;
    private double longitude;
    private LocalDateTime timestamp;

    public LocationUpdate() {}

    public LocationUpdate(String username, double latitude, double longitude, LocalDateTime timestamp) {
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public void setUserId(String userId) { this.username = userId; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}

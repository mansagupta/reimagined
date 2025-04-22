package com.example.safetyAlert.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "fcm_tokens")
public class FCMToken {
    @Id
    private String id;
    private String username;
    private String token;
    private String mobile;

    public FCMToken() {}

    public FCMToken(String mobile, String token) {
        this.mobile = mobile;
        this.token = token;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
}

package com.example.safetyAlert.model;

public class AuthResponse {
    private String token;
    private String mobile;


    public AuthResponse(String token, String mobile) {
        this.token = token;
        this.mobile = mobile;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
}

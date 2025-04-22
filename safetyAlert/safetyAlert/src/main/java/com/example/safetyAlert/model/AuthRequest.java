package com.example.safetyAlert.model;

public class AuthRequest {
    private String password;
    private String mobile;

    public AuthRequest() {}

    public AuthRequest(String password, String mobile) {
        this.password = password;
        this.mobile = mobile;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getMobile() { return mobile; }

    public void setMobile(String mobile) { this.mobile = mobile; }
}

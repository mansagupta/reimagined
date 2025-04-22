package com.example.safetyAlert.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Users")
public class User {
    private String username;
    private String email;
    private String password;
    private String mobile;

    public User() {

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User(String username,String email, String password, String mobile) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
}

package com.example.safetyAlert.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "emergency_contacts")
public class EmergencyContact {
    @Id
    private String username;
    private List<Contact> contacts;

    public EmergencyContact(String username, List<Contact> contacts) {
        this.username= username;
        this.contacts = contacts;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username= username; }

    public List<Contact> getContacts() { return contacts; }
    public void setContacts(List<Contact> contacts) { this.contacts = contacts; }

    public static class Contact {
        private String name;
        private String phone;

        public Contact(String name, String phone) {
            this.name = name;
            this.phone = phone;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
    }
}

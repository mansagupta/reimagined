package com.example.safetyAlert.controller;

import com.example.safetyAlert.model.EmergencyContact;
import com.example.safetyAlert.service.EmergencyContactService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergency")
public class EmergencyContactController {
    private final EmergencyContactService contactService;

    public EmergencyContactController(EmergencyContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/save")
    public String saveContacts(@RequestParam String mobile, @RequestBody List<EmergencyContact.Contact> contacts) {
        contactService.saveContacts(mobile, contacts);
        return "Emergency contacts saved successfully!";
    }

    @GetMapping("/get")
    public List<EmergencyContact.Contact> fetchContacts(@RequestParam String mobile) {
        return contactService.fetchContacts(mobile);
    }

    @DeleteMapping("/{username}/{phone}")
    public String deleteContact(@PathVariable String mobile, @PathVariable String phone) {
        contactService.deleteContact(mobile, phone);
        return "Contact deleted successfully";
    }

}

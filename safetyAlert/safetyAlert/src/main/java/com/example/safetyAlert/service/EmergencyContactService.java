package com.example.safetyAlert.service;

import com.example.safetyAlert.model.EmergencyContact;
import com.example.safetyAlert.repository.EmergencyContactRepository;
import com.example.safetyAlert.repository.EmergencyContactSearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmergencyContactService {
    private final EmergencyContactRepository contactRepository;
    private final EmergencyContactSearchRepository contactSearchRepository;

    public EmergencyContactService(EmergencyContactRepository contactRepository, EmergencyContactSearchRepository contactSearchRepository) {
        this.contactRepository = contactRepository;
        this.contactSearchRepository = contactSearchRepository;
    }

    public void saveContacts(String mobile, List<EmergencyContact.Contact> newContacts) {
        Optional<EmergencyContact> existing = contactSearchRepository.findByUsername(mobile);

        if (existing.isPresent()) {
            EmergencyContact emergencyContact = existing.get();
            List<EmergencyContact.Contact> contacts = emergencyContact.getContacts();
            contacts.addAll(newContacts); // Append new contacts
            emergencyContact.setContacts(contacts);
            contactRepository.save(emergencyContact);
        } else {
            contactRepository.save(new EmergencyContact(mobile, newContacts));
        }
    }

    public List<EmergencyContact.Contact> fetchContacts(String mobile) {
        Optional<EmergencyContact> existing = contactSearchRepository.findByUsername(mobile);

        if (existing.isPresent()) {
            System.out.println("Fetched contacts: " + existing.get().getContacts());
            return existing.get().getContacts();
        } else {
            System.out.println("No contacts found for " + mobile);
            return null;
        }
    }


    public void deleteContact(String mobile, String phone) {
        Optional<EmergencyContact> existing = contactSearchRepository.findByUsername(mobile);

        if (existing.isPresent()) {
            EmergencyContact emergencyContact = existing.get();
            List<EmergencyContact.Contact> contacts = emergencyContact.getContacts();

            System.out.println("Before deletion: " + contacts);


            boolean removed = contacts.removeIf(contact -> contact.getPhone().equals(phone));

            if (removed) {
                System.out.println("After deletion: " + contacts);

                if (contacts.isEmpty()) {
                    contactRepository.deleteById(mobile);
                    System.out.println("Deleted the whole document for username: " + mobile);
                } else {
                    emergencyContact.setContacts(contacts);
                    contactRepository.save(emergencyContact);
                    System.out.println("Updated document after deletion: " + emergencyContact);
                }
            } else {
                System.out.println("Contact not found for deletion: " + phone);
            }
        } else {
            System.out.println("No user found with username: " + mobile);
        }
    }



}

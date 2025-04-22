package com.example.safetyAlert.repository;

import com.example.safetyAlert.model.EmergencyContact;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmergencyContactRepository extends MongoRepository<EmergencyContact, String> {
}

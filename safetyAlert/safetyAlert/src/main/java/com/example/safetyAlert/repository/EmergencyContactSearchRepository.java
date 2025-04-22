package com.example.safetyAlert.repository;

import com.example.safetyAlert.model.EmergencyContact;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface EmergencyContactSearchRepository {

    @Query("{ 'Mobile': ?0")
    Optional<EmergencyContact> findByUsername(String mobile);

}

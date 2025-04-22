package com.example.safetyAlert.repository;

import com.example.safetyAlert.model.EmergencyContact;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EmergencyContactRepositorySearchImpl implements EmergencyContactSearchRepository {

    @Autowired
    MongoTemplate client;

    @Override
     public Optional<EmergencyContact> findByUsername(String Mobile) {

        Query query = new Query();
        query.addCriteria(Criteria.where("Mobile").is(Mobile));
        EmergencyContact emergencyContact = client.findOne(query, EmergencyContact.class, "emergency_contacts");
        return Optional.ofNullable(emergencyContact);
    }

}

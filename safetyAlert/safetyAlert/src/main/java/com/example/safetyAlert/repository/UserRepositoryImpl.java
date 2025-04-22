package com.example.safetyAlert.repository;


import com.example.safetyAlert.model.EmergencyContact;
import com.example.safetyAlert.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public abstract class UserRepositoryImpl implements UserRepository{

    @Autowired
    MongoTemplate client;

    @Override
    public void deleteByUsername(User existingUser) {
        String username = existingUser.getUsername();

        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));

        client.remove(query, User.class);
    }

}


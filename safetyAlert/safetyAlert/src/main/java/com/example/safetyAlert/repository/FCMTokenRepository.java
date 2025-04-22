package com.example.safetyAlert.repository;

import com.example.safetyAlert.model.FCMToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface FCMTokenRepository extends MongoRepository<FCMToken, String> {
    Optional<FCMToken> findByMobile(String mobile);
}

package com.example.safetyAlert.repository;

import com.example.safetyAlert.model.SOSRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SOSRepository extends MongoRepository<SOSRequest, String> {
    List<SOSRequest> findByUsername(String username);
}

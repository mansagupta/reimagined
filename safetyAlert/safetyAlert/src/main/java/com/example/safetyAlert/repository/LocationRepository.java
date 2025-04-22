package com.example.safetyAlert.repository;

import com.example.safetyAlert.model.LocationUpdate;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface LocationRepository extends MongoRepository<LocationUpdate, String> {
    List<LocationUpdate> findByUsernameOrderByTimestampDesc(String username);
}

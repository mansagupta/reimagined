package com.example.safetyAlert.repository;

import com.example.safetyAlert.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByMobile(String mobile);

    Optional<User> findByUsername(String username);


    void deleteByUsername(User existingUser);
}

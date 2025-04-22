package com.example.safetyAlert.repository;

import com.example.safetyAlert.model.MediaFile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MediaRepository extends MongoRepository<MediaFile, String> {
}

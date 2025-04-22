package com.example.safetyAlert.service;


import com.example.safetyAlert.model.FCMToken;
import com.example.safetyAlert.repository.FCMTokenRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class FCMTokenService {
    private final FCMTokenRepository tokenRepository;

    public FCMTokenService(FCMTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void saveToken(String token, String mobile) {
        Optional<FCMToken> existingToken = tokenRepository.findByMobile(mobile);
        if (existingToken.isPresent()) {
            existingToken.get().setToken(token);
            tokenRepository.save(existingToken.get());
        } else {
            tokenRepository.save(new FCMToken(mobile, token));
        }
    }

    public String getToken(String mobile) {
        return tokenRepository.findByMobile(mobile).map(FCMToken::getToken).orElse(null);
    }
}


package com.example.safetyAlert.service;

import com.example.safetyAlert.model.User;
import com.example.safetyAlert.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByMobile(mobile);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with mobile: " + mobile);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.get().getMobile())
                .password(user.get().getPassword())
                .roles("USER")
                .build();
    }

    public String getUsernameByMobile(String mobile) {
        Optional<User> user = userRepository.findByMobile(mobile);
        return user.map(User::getUsername).orElse("unknown");
    }
}


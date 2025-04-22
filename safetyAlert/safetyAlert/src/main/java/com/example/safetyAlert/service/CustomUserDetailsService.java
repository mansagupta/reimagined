package com.example.safetyAlert.service;

import com.example.safetyAlert.model.User;
import com.example.safetyAlert.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        User user = userRepository.findByMobile(mobile)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with mobile: " + mobile));
        return new org.springframework.security.core.userdetails.User(
                user.getMobile(), user.getPassword(), new ArrayList<>());
    }
}

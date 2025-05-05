package com.mosuuuutech.CRUD.API.services;

import com.mosuuuutech.CRUD.API.entity.Auth;
import com.mosuuuutech.CRUD.API.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Auth registerNewUser(Auth auth) {
        if (userRepository.existByUsername(auth.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        auth.setPassword(passwordEncoder.encode(auth.getPassword()));

        auth.setRole("user");

        return userRepository.save(auth);
    }

    public Auth findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}
package com.example.spring_custom_authentication_test.service;

import com.example.spring_custom_authentication_test.UserException;
import com.example.spring_custom_authentication_test.entity.User;
import com.example.spring_custom_authentication_test.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public  AuthenticationService(
            PasswordEncoder passwordEncoder,
            UserRepo userRepo,
            AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public User register(User user) {
        Optional<User> userExist = userRepo.findByEmail(user.getEmail());
        if(userExist.isPresent()) {
            throw new UserException("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return user;
    }


    public User authenticate(User user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword()
        ));

        return userRepo.findByEmail(user.getEmail()).orElseThrow();
    }

}

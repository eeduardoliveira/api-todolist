package com.example.demo.service;


import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.demo.domain.model.User;
import com.example.demo.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private IUserRepository userRepository;

    public IUserRepository getUserRepository() {
        return userRepository;
    }

    public User signIn (User user) throws Exception {
        logger.info("Attempting to register new user: {}", user.getUsername());
        
        User userCreated = this.userRepository.findByUsername(user.getUsername());
        if (userCreated != null) {
            logger.warn("User registration failed: Username '{}' already exists", user.getUsername());
            throw new Exception("Usuario já  nao existente");
        }

        logger.debug("Hashing password for user: {}", user.getUsername());
        String passwordHashred = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
        user.setPassword(passwordHashred);
        
        User savedUser = this.userRepository.save(user);
        logger.info("User successfully registered: {} (ID: {})", savedUser.getUsername(), savedUser.getId());
        
        return savedUser;
    }
}

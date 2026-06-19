package com.example.demo.controller;

import com.example.demo.domain.model.User;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("usuario")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping(path = {"sign-in"})
    public ResponseEntity sign(@RequestBody User user) throws Exception {
        logger.info("Received sign-in request for username: {}", user.getUsername());
        logger.debug("User registration request details: name='{}'", user.getName());
        
        userService.signIn(user);
        
        logger.info("User sign-in successful: {}", user.getUsername());
        return ResponseEntity.ok(user);
    }
}

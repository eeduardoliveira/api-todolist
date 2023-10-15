package com.example.demo.controller;

import com.example.demo.domain.model.User;
import com.example.demo.service.UserService;
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

    @Autowired
    private UserService userService;

    @PostMapping(path = {"sign-in"})
    public ResponseEntity sign(@RequestBody User user) throws Exception {
        userService.signIn(user);
        return  ResponseEntity.ok(user);
    }
}

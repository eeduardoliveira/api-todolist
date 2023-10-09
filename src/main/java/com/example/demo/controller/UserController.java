package com.example.demo.controller;

import com.example.demo.domain.model.User;
import com.example.demo.service.UserService;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void sign(@RequestBody User user) {
        userService.signIn(user);
    }
}

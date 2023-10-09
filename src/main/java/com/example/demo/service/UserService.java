package com.example.demo.service;


import com.example.demo.domain.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public void signIn (User user) {
        System.out.println(user.getName());
    }
}

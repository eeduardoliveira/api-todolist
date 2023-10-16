package com.example.demo.service;


import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.demo.domain.model.User;
import com.example.demo.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    public IUserRepository getUserRepository() {
        return userRepository;
    }

    public User signIn (User user) throws Exception {
        User userCreated = this.userRepository.findByUsername(user.getUsername());
        if (userCreated != null) {
            throw new Exception("Usuario já  nao existente");
        }

       String passwordHashred = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
        user.setPassword(passwordHashred);
        return this.userRepository.save(user);
    }
}

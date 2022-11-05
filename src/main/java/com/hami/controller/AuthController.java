package com.hami.controller;

import com.hami.model.User;
import com.hami.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping(value = "/hello")
    public String hello() {
        return "Hello";
    }

    @PostMapping(value = "/register")
    public User register(@RequestBody User user) {
        return userRepository.save(user);
    }

}

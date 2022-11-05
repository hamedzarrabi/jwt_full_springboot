package com.hami.service;

import com.hami.model.User;
import com.hami.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String firstName, String lastName, String email, String password, String passwordConfirm) {

        if (!Objects.equals(password,passwordConfirm)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password do not match");
        }

        return userRepository.save(
                User.of(firstName, lastName, email, password)
        );
    }
}

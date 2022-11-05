package com.hami.service;

import com.hami.exception.EmailAlreadyExistsError;
import com.hami.exception.InvalidCredentialsError;
import com.hami.exception.PasswordDontMatchError;
import com.hami.model.User;
import com.hami.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String accessTokenSecret;
    private final String refreshTokenSecret;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       @Value("${application.security.access-token-secret}") String accessTokenSecret,
                       @Value("${application.security.refresh-token-secret}") String refreshTokenSecret) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.accessTokenSecret = accessTokenSecret;
        this.refreshTokenSecret = refreshTokenSecret;
    }

    public User register(String firstName, String lastName, String email, String password, String passwordConfirm) {

        if (!Objects.equals(password, passwordConfirm)) {
            throw new PasswordDontMatchError();
        }

        User user;

        try {
            user = userRepository.save(
                    User.of(firstName, lastName, email, passwordEncoder.encode(password))
            );
        } catch (DbActionExecutionException exception) {
            throw new EmailAlreadyExistsError();
        }

        return user;
    }

    public Login login(String email, String password) {
//        find user by email
        var user = userRepository.findByEmail(email).orElseThrow(InvalidCredentialsError :: new);

//        see if the password match
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsError();
        }

//        return user
        return Login.of(user.getId(),   accessTokenSecret, refreshTokenSecret);
    }
}

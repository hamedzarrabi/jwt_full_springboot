package com.hami.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hami.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    record RegisterRequest(@JsonProperty("first_name") String firstName,@JsonProperty("last_name") String lastName,String email,String password, @JsonProperty("password_confirm") String passwordConfirm) { }
    record  RegisterResponse(Long id, @JsonProperty("first_name") String firstName,@JsonProperty("last_name") String lastName,String email, String password) {}

    @PostMapping(value = "/register")
    public RegisterResponse register(@RequestBody RegisterRequest registerRequest) {

         var user = userService.register(

                        registerRequest.firstName(),
                        registerRequest.lastName(),
                        registerRequest.email(),
                        registerRequest.password(),
                        registerRequest.passwordConfirm()

        );

        return new RegisterResponse(user.getId(), user.getFirstName(),  user.getLastName(), user.getEmail(), user.getPassword());
    }

    record LoginRequest(String email, String password){}
    record  LoginResponse(String token) {}
    @PostMapping(value = "/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        var login = userService.login(loginRequest.email(), loginRequest.password());

        Cookie cookie = new Cookie("refresh_token", login.getRefreshToken().getToken());
        cookie.setMaxAge(3600);
        cookie.setHttpOnly(true);
        cookie.setPath("/api");

        response.addCookie(cookie);

        return new LoginResponse(login.getAccessToken().getToken());
    }

}

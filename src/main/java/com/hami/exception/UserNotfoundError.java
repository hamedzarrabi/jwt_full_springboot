package com.hami.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotfoundError extends ResponseStatusException {
    public UserNotfoundError() {
        super(HttpStatus.BAD_REQUEST, "User not found");
    }
}

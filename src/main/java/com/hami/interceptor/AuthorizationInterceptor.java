package com.hami.interceptor;

import com.hami.exception.NoBearerTokenError;
import com.hami.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    private final UserService userService;


    public AuthorizationInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new NoBearerTokenError();
        }
        request.setAttribute("user", userService.getUserFromToken(authorizationHeader.substring(7)));

        return true;
    }

}

package com.FelipeLohan.ecommerce.services;

import org.springframework.stereotype.Service;

import com.FelipeLohan.ecommerce.entities.User;
import com.FelipeLohan.ecommerce.services.exceptions.ForbiddenException;
import com.FelipeLohan.ecommerce.services.interfaces.AuthService;
import com.FelipeLohan.ecommerce.services.interfaces.UserService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    public AuthServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void validateSelfOrAdmin(long userId) {
        User me = userService.authenticated();
        if (!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)) {
            throw new ForbiddenException("Access denied");
        }
    }
}

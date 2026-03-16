package com.FelipeLohan.ecommerce.controllers.interfaces;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.FelipeLohan.ecommerce.dto.UserDTO;
import com.FelipeLohan.ecommerce.dto.UserInsertDTO;

@RequestMapping(value = "/users")
public interface UserController {

    @GetMapping(value = "/me")
    ResponseEntity<UserDTO> getMe();

    @PostMapping(value = "/register")
    ResponseEntity<UserDTO> register(@Valid @RequestBody UserInsertDTO dto);
}

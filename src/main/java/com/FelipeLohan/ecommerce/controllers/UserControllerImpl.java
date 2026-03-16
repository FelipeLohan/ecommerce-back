package com.FelipeLohan.ecommerce.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.FelipeLohan.ecommerce.controllers.interfaces.UserController;
import com.FelipeLohan.ecommerce.dto.UserDTO;
import com.FelipeLohan.ecommerce.dto.UserInsertDTO;
import com.FelipeLohan.ecommerce.services.interfaces.UserService;

@RestController
public class UserControllerImpl implements UserController {

    @Autowired
    private UserService service;

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<UserDTO> getMe() {
        UserDTO dto = service.getMe();
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<UserDTO> register(UserInsertDTO dto) {
        UserDTO result = service.register(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/users/{id}")
                .buildAndExpand(result.getId())
                .toUri();
        return ResponseEntity.created(uri).body(result);
    }
}

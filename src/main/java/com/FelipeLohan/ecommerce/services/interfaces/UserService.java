package com.FelipeLohan.ecommerce.services.interfaces;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.FelipeLohan.ecommerce.dto.UserDTO;
import com.FelipeLohan.ecommerce.dto.UserInsertDTO;
import com.FelipeLohan.ecommerce.entities.User;

public interface UserService extends UserDetailsService {

    User authenticated();

    UserDTO getMe();

    UserDTO register(UserInsertDTO dto);
}

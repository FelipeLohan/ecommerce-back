package com.FelipeLohan.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.FelipeLohan.ecommerce.dto.UserDTO;
import com.FelipeLohan.ecommerce.dto.UserInsertDTO;
import com.FelipeLohan.ecommerce.entities.Role;
import com.FelipeLohan.ecommerce.entities.User;
import com.FelipeLohan.ecommerce.mappers.UserMapper;
import com.FelipeLohan.ecommerce.repositories.RoleRepository;
import com.FelipeLohan.ecommerce.repositories.UserRepository;
import com.FelipeLohan.ecommerce.services.exceptions.EmailAlreadyExistsException;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = repository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Email not found");
        }
        return user;
    }

    protected User authenticated() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return repository.findByEmail(username);
        }
        catch (Exception e) {
            throw new UsernameNotFoundException("Invalid user");
        }
    }

    @Transactional(readOnly = true)
    public UserDTO getMe() {
        User entity = authenticated();
        return userMapper.toDTO(entity);
    }

    @Transactional
    public UserDTO register(UserInsertDTO dto) {
        if (repository.findByEmail(dto.getEmail()) != null) {
            throw new EmailAlreadyExistsException("Email já cadastrado: " + dto.getEmail());
        }

        User entity = userMapper.toEntity(dto);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));

        Role role = roleRepository.findByAuthority("ROLE_CLIENT");
        entity.getRoles().add(role);

        entity = repository.save(entity);
        return userMapper.toDTO(entity);
    }
}

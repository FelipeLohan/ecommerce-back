package com.FelipeLohan.ecommerce.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import com.FelipeLohan.ecommerce.services.interfaces.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository repository,
                           RoleRepository roleRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           UserMapper userMapper) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Email not found");
        }
        return user;
    }

    @Override
    public User authenticated() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return repository.findByEmail(username);
        }
        catch (Exception e) {
            throw new UsernameNotFoundException("Invalid user");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getMe() {
        User entity = authenticated();
        return userMapper.toDTO(entity);
    }

    @Override
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

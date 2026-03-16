package com.FelipeLohan.ecommerce.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.FelipeLohan.ecommerce.dto.UserDTO;
import com.FelipeLohan.ecommerce.dto.UserInsertDTO;
import com.FelipeLohan.ecommerce.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", expression = "java(entity.getAuthorities().stream().map(a -> a.getAuthority()).collect(java.util.stream.Collectors.toList()))")
    UserDTO toDTO(User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User toEntity(UserInsertDTO dto);
}

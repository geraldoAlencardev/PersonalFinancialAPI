package com.Alencar.demo.mapper;

import com.Alencar.demo.dto.user.UserCreateDTO;
import com.Alencar.demo.dto.user.UserResponseDTO;
import com.Alencar.demo.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserCreateDTO dto) {
        return User.builder()
                .username(dto.username())
                .email(dto.email())
                .password(dto.password())
                .build();
    }

    public UserResponseDTO userResponseDTO(User entity) {
        return new UserResponseDTO(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail()
        );
    }
}

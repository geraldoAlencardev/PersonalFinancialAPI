package com.Alencar.demo.controller;

import com.Alencar.demo.dto.user.UserCreateDTO;
import com.Alencar.demo.dto.user.UserPasswordUpdateDTO;
import com.Alencar.demo.dto.user.UserResponseDTO;
import com.Alencar.demo.mapper.UserMapper;
import com.Alencar.demo.model.User;
import com.Alencar.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserCreateDTO dto){

        User userEntity = userMapper.toEntity(dto);
        User savedUser = userService.createUser(userEntity);
        UserResponseDTO response = userMapper.userResponseDTO(savedUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id){
        User user = userService.findById(id);
        UserResponseDTO response = userMapper.userResponseDTO(user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UserCreateDTO dto){

        User userDetails = userMapper.toEntity(dto);
        User userUpdated = userService.updateUser(id, userDetails);
        UserResponseDTO response = userMapper.userResponseDTO(userUpdated);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UserPasswordUpdateDTO dto){

        userService.updatePassword(
                id,
                dto.currentPassword(),
                dto.newPassword(),
                dto.confirmPassword()
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

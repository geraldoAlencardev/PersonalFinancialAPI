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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> findMe(@AuthenticationPrincipal User loggedUser){
        UserResponseDTO response = userMapper.userResponseDTO(loggedUser);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> update(@AuthenticationPrincipal User loggedUser,
                                                  @Valid @RequestBody UserCreateDTO dto){

        User userDetails = userMapper.toEntity(dto);
        User userUpdated = userService.updateUser(loggedUser.getId(), userDetails);
        UserResponseDTO response = userMapper.userResponseDTO(userUpdated);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal User loggedUser,
                                               @Valid @RequestBody UserPasswordUpdateDTO dto){

        userService.updatePassword(
                loggedUser.getId(),
                dto.currentPassword(),
                dto.newPassword(),
                dto.confirmPassword()
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal User loggedUser){
        userService.deleteUser(loggedUser.getId());
        return ResponseEntity.noContent().build();
    }
}

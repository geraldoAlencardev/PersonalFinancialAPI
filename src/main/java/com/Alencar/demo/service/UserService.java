package com.Alencar.demo.service;

import com.Alencar.demo.infrastructure.exceptions.BusinessException;
import com.Alencar.demo.infrastructure.exceptions.ResourceNotFoundException;
import com.Alencar.demo.model.User;
import com.Alencar.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado"));
    }

    @Transactional
    public User createUser(User user) {

        if(userRepository.existsByEmail(user.getEmail())) {
            throw new BusinessException("Este E-mail já esta cadastrado");
        }

        if(userRepository.existsByUsername(user.getUsername())) {
            throw new BusinessException("Este UserName já esta cadastrado");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = findById(id);

        if(!user.getEmail().equals(userDetails.getEmail())) {
            userRepository.findByEmail(userDetails.getEmail())
                    .ifPresent(existingUser -> {
                        if(!existingUser.getId().equals(id)){
                            throw new BusinessException("Este E-mail já esta cadastrado");
                        }
                    });
            user.setEmail(userDetails.getEmail());
        }

        if(!user.getUsername().equals(userDetails.getUsername())) {
            userRepository.findByUsername(userDetails.getUsername())
                    .ifPresent(existingUser -> {
                        if(!existingUser.getId().equals(id)){
                            throw new BusinessException("Este Username já está cadastrado");
                        }
                    });
            user.setUsername(userDetails.getUsername());
        }


        return userRepository.save(user);
    }

    public void updatePassword(Long id, String oldPassword, String newPassword, String confirmPassword) {

        if(!newPassword.equals(confirmPassword)) {
            throw new BusinessException("A nova senha e a confirmação não conferem");
        }

        User user = findById(id);

        if(!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("A senha atual esttá incorreta");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }
}

package com.ijse.adlync.service.impl;

import com.ijse.adlync.dto.request.LoginRequestDTO;
import com.ijse.adlync.dto.request.RegisterRequestDTO;
import com.ijse.adlync.dto.response.LoginResponseDTO;
import com.ijse.adlync.entity.UserEntity;
import com.ijse.adlync.repository.UserRepository;
import com.ijse.adlync.service.AuthService;
import com.ijse.adlync.service.EmailService;
import com.ijse.adlync.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;


    @Override
    public LoginResponseDTO authenticate(LoginRequestDTO loginRequestDTO) {
        UserEntity userEntity;

        if (loginRequestDTO.getUsername().contains("@")) {
            userEntity = userRepository.findByEmail(loginRequestDTO.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Email not found"));
        } else {
            userEntity = userRepository.findByUsername(loginRequestDTO.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        }

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), userEntity.getPassword())) {
            throw new BadCredentialsException("Invalid username/email or password");
        }

        String token = jwtUtil.generateToken(userEntity);
        return new LoginResponseDTO(token, userEntity.getRole());
    }

    @Override
    public String register(RegisterRequestDTO registerRequestDTO) {

        if (registerRequestDTO.getUsername() == null || registerRequestDTO.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (registerRequestDTO.getPassword() == null || registerRequestDTO.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (registerRequestDTO.getName() == null || registerRequestDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (registerRequestDTO.getEmail() == null || registerRequestDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        UserEntity userEntity = UserEntity.builder()
                .username(registerRequestDTO.getUsername())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .role(registerRequestDTO.getRole())
                .name(registerRequestDTO.getName())
                .email(registerRequestDTO.getEmail())
                .build();
        userRepository.save(userEntity);
        emailService.sendSignupEmail(userEntity.getEmail(), userEntity.getName());
        return "User Registration Success";
    }
}

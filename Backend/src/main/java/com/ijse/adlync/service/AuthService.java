package com.ijse.adlync.service;

import com.ijse.adlync.dto.request.LoginRequestDTO;
import com.ijse.adlync.dto.request.RegisterRequestDTO;
import com.ijse.adlync.dto.response.LoginResponseDTO;
import com.ijse.adlync.entity.UserEntity;
import com.ijse.adlync.entity.enums.UserEntityRoleEnum;
import com.ijse.adlync.repository.UserRepository;
import com.ijse.adlync.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponseDTO authenticate(LoginRequestDTO loginRequestDTO) {
        UserEntity userEntity =
                userRepository.findByUsername(loginRequestDTO.getUsername())
                        .orElseThrow(
                                () -> new UsernameNotFoundException
                                        ("Username not found"));
        if (!passwordEncoder.matches(
                loginRequestDTO.getPassword(),
                userEntity.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }
        String token = jwtUtil.generateToken(userEntity);
        return new LoginResponseDTO(token);
    }

    public String register(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.findByUsername(
                registerRequestDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        UserEntity userEntity = UserEntity.builder()
                .username(registerRequestDTO.getUsername())
                .password(passwordEncoder.encode(
                        registerRequestDTO.getPassword()))
                .role(UserEntityRoleEnum.valueOf(String.valueOf(registerRequestDTO.getRole())))
                .build();
        userRepository.save(userEntity);
        return "User Registration Success";
    }
}

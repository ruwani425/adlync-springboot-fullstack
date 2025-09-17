package com.ijse.adlync.service.impl;

import com.ijse.adlync.dto.request.RegisterRequestDTO;
import com.ijse.adlync.dto.response.RegisterResponseDTO;
import com.ijse.adlync.dto.response.UserResponseDTO;
import com.ijse.adlync.entity.UserEntity;
import com.ijse.adlync.entity.enums.UserEntityRoleEnum;
import com.ijse.adlync.repository.UserRepository;
import com.ijse.adlync.service.EmailService;
import com.ijse.adlync.service.OtpService;
import com.ijse.adlync.service.UserService;
import com.ijse.adlync.util.ValueEncoder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
    private static final int PASSWORD_LENGTH = 10;

    private final UserRepository repository;
    private final ValueEncoder valueEncoder;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ModelMapper modelMapper;
    private final OtpService otpService;

    @Override
    public List<RegisterResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDTO> findAllUsers() {
        return repository.findAll().stream()
                .map(this::toUserResponseDTO)
                .collect(Collectors.toList());
    }


    @Override
    public RegisterResponseDTO findById(Long id) {
        UserEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    @Override
    public RegisterResponseDTO create(RegisterRequestDTO requestDTO) throws Exception {
        UserEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        emailService.sendModeratorSignupEmail(entity.getEmail(), entity.getName());
        return toResponseDTO(entity);
    }

    @Override
    public RegisterResponseDTO update(Long id, RegisterRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("UserEntity not found with id: " + id);
        }
        UserEntity entity = toEntity(requestDTO);
        entity.setId(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("UserEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public UserResponseDTO updateModerator(String token, String password) throws Exception {
        String email = valueEncoder.decrypt(token);
        UserEntity user = repository.findByEmail(email).orElseThrow(() -> new Exception("UserEntity not found with email: " + email));
        user.setPassword(passwordEncoder.encode(password));
        repository.save(user);
        System.out.println(user);
        return toUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO getUserByUsername(String username) {
        UserEntity user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return modelMapper.map(user, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO updateProfilePhoto(String username, String profileImageUrl) {
        UserEntity user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setProfileImageUrl(profileImageUrl);
        repository.save(user);

        return toUserResponseDTO(user);
    }

    @Override
    public void resetPassword(String email, String newPassword) throws Exception {
        if (!checkEmailExists(email)) {
            throw new Exception("Email not found in the system.");
        }
        UserEntity user = repository.findByEmail(email)
                .orElseThrow(() -> new Exception("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        repository.save(user);
    }

    @Override
    public boolean checkEmailExists(String email) {
        return repository.findByEmail(email).isPresent();
    }

    private RegisterResponseDTO toResponseDTO(UserEntity entity) {
        RegisterResponseDTO dto = new RegisterResponseDTO();
        dto.setId(entity.getId());
        dto.setPassword(entity.getPassword());
        dto.setRole(entity.getRole());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        return dto;
    }

    private UserResponseDTO toUserResponseDTO(UserEntity entity) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setJoinDate(entity.getJoinDate());
        dto.setRole(entity.getRole());
        dto.setProfileImageUrl(entity.getProfileImageUrl());
        return dto;
    }


    private UserEntity toEntity(RegisterRequestDTO dto) {
        UserEntity entity = new UserEntity();
        String rawPassword = generateRandomPassword();
        entity.setPassword(passwordEncoder.encode(rawPassword));
        entity.setRole(UserEntityRoleEnum.MODERATOR);
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setUsername(dto.getEmail());
        entity.setStatus("ACTIVE");
        return entity;
    }

    private String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

}

package com.ijse.adlync.service;

import com.ijse.adlync.dto.request.RegisterRequestDTO;
import com.ijse.adlync.dto.response.RegisterResponseDTO;
import com.ijse.adlync.dto.response.UserResponseDTO;

import java.util.List;

public interface UserService {

    List<RegisterResponseDTO> findAll();

    List<UserResponseDTO> findAllUsers();

    RegisterResponseDTO findById(Long id);

    RegisterResponseDTO create(RegisterRequestDTO requestDTO) throws Exception;

    RegisterResponseDTO update(Long id, RegisterRequestDTO requestDTO);

    void deleteById(Long id);

    UserResponseDTO updateModerator(String token, String password) throws Exception;

    UserResponseDTO getUserByUsername(String username);

    UserResponseDTO updateProfilePhoto(String username, String profileImageUrl);
    void resetPassword(String email, String newPassword) throws Exception;
    public boolean checkEmailExists(String email);
    }

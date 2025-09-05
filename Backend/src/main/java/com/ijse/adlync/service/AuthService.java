package com.ijse.adlync.service;

import com.ijse.adlync.dto.request.LoginRequestDTO;
import com.ijse.adlync.dto.request.RegisterRequestDTO;
import com.ijse.adlync.dto.response.LoginResponseDTO;


public interface AuthService {
    LoginResponseDTO authenticate(LoginRequestDTO loginRequestDTO);

    String register(RegisterRequestDTO registerRequestDTO);
}
package com.ijse.adlync.controller;

import com.ijse.adlync.dto.request.LoginRequestDTO;
import com.ijse.adlync.dto.request.RegisterRequestDTO;
import com.ijse.adlync.dto.response.ApiResponseDTO;
import com.ijse.adlync.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO> registerUser(
            @RequestBody RegisterRequestDTO registerRequestDTO) {
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                authService.register(registerRequestDTO)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO> login(
            @RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "OK",
                authService.authenticate(loginRequestDTO)));
    }
}

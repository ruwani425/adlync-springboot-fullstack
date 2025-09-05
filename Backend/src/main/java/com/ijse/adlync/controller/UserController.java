package com.ijse.adlync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ijse.adlync.dto.request.RegisterRequestDTO;
import com.ijse.adlync.dto.response.RegisterResponseDTO;
import com.ijse.adlync.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(name = "UserEntity Management", description = "APIs for managing UserEntity entities")
public class UserController {

    @Autowired
    private UserServiceImpl service;

    @GetMapping
    @Operation(summary = "Get all Users", description = "Retrieve a list of all User entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Users"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<RegisterResponseDTO>> getAllUsers() {
        List<RegisterResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get User by ID", description = "Retrieve a User entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved User"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<RegisterResponseDTO> getUserById(@Parameter(description = "ID of the User to retrieve") @PathVariable Long id) {
        RegisterResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new User", description = "Create a new User entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created User"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<RegisterResponseDTO> createUser(@Parameter(description = "User data to create") @RequestBody RegisterRequestDTO requestDTO) {
        RegisterResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update User", description = "Update an existing User entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated User"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<RegisterResponseDTO> updateUser(@Parameter(description = "ID of the User to update") @PathVariable Long id, @Parameter(description = "Updated User data") @RequestBody RegisterRequestDTO requestDTO) {
        RegisterResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete User", description = "Delete a User entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted User"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteUser(@Parameter(description = "ID of the User to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

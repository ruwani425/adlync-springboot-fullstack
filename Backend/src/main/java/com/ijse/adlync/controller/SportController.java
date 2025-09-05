package com.ijse.adlync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ijse.adlync.dto.request.SportRequestDTO;
import com.ijse.adlync.dto.response.SportResponseDTO;
import com.ijse.adlync.service.impl.SportServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/sports")
@Tag(name = "SportEntity Management", description = "APIs for managing SportEntity entities")
public class SportController {

    @Autowired
    private SportServiceImpl service;

    @GetMapping
    @Operation(summary = "Get all Sports", description = "Retrieve a list of all Sport entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Sports"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<SportResponseDTO>> getAllSports() {
        List<SportResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Sport by ID", description = "Retrieve a Sport entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Sport"),
        @ApiResponse(responseCode = "404", description = "Sport not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SportResponseDTO> getSportById(@Parameter(description = "ID of the Sport to retrieve") @PathVariable Long id) {
        SportResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Sport", description = "Create a new Sport entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Sport"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SportResponseDTO> createSport(@Parameter(description = "Sport data to create") @RequestBody SportRequestDTO requestDTO) {
        SportResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Sport", description = "Update an existing Sport entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Sport"),
        @ApiResponse(responseCode = "404", description = "Sport not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SportResponseDTO> updateSport(@Parameter(description = "ID of the Sport to update") @PathVariable Long id, @Parameter(description = "Updated Sport data") @RequestBody SportRequestDTO requestDTO) {
        SportResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Sport", description = "Delete a Sport entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted Sport"),
        @ApiResponse(responseCode = "404", description = "Sport not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteSport(@Parameter(description = "ID of the Sport to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

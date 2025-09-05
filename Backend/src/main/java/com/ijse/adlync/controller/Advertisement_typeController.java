package com.ijse.adlync.controller;

import com.ijse.adlync.service.Advertisement_typeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ijse.adlync.dto.request.Advertisement_typeRequestDTO;
import com.ijse.adlync.dto.response.Advertisement_typeResponseDTO;
import com.ijse.adlync.service.impl.Advertisement_typeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/advertisement_types")
@Tag(name = "Advertisement_typeEntity Management", description = "APIs for managing Advertisement_typeEntity entities")
public class Advertisement_typeController {

    @Autowired
    private Advertisement_typeService service;

    @GetMapping
    @Operation(summary = "Get all Advertisement_types", description = "Retrieve a list of all Advertisement_type entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Advertisement_types"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Advertisement_typeResponseDTO>> getAllAdvertisement_types() {
        List<Advertisement_typeResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Advertisement_type by ID", description = "Retrieve a Advertisement_type entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Advertisement_type"),
        @ApiResponse(responseCode = "404", description = "Advertisement_type not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Advertisement_typeResponseDTO> getAdvertisement_typeById(@Parameter(description = "ID of the Advertisement_type to retrieve") @PathVariable Long id) {
        Advertisement_typeResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Advertisement_type", description = "Create a new Advertisement_type entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Advertisement_type"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Advertisement_typeResponseDTO> createAdvertisement_type(@Parameter(description = "Advertisement_type data to create") @RequestBody Advertisement_typeRequestDTO requestDTO) {
        Advertisement_typeResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Advertisement_type", description = "Update an existing Advertisement_type entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Advertisement_type"),
        @ApiResponse(responseCode = "404", description = "Advertisement_type not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Advertisement_typeResponseDTO> updateAdvertisement_type(@Parameter(description = "ID of the Advertisement_type to update") @PathVariable Long id, @Parameter(description = "Updated Advertisement_type data") @RequestBody Advertisement_typeRequestDTO requestDTO) {
        Advertisement_typeResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Advertisement_type", description = "Delete a Advertisement_type entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted Advertisement_type"),
        @ApiResponse(responseCode = "404", description = "Advertisement_type not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteAdvertisement_type(@Parameter(description = "ID of the Advertisement_type to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

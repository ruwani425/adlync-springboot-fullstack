package com.ijse.adlync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ijse.adlync.dto.request.EntertaintmentRequestDTO;
import com.ijse.adlync.dto.response.EntertaintmentResponseDTO;
import com.ijse.adlync.service.impl.EntertaintmentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/entertaintments")
@Tag(name = "EntertaintmentEntity Management", description = "APIs for managing EntertaintmentEntity entities")
public class EntertaintmentController {

    @Autowired
    private EntertaintmentServiceImpl service;

    @GetMapping
    @Operation(summary = "Get all Entertaintments", description = "Retrieve a list of all Entertaintment entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Entertaintments"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<EntertaintmentResponseDTO>> getAllEntertaintments() {
        List<EntertaintmentResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Entertaintment by ID", description = "Retrieve a Entertaintment entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Entertaintment"),
        @ApiResponse(responseCode = "404", description = "Entertaintment not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<EntertaintmentResponseDTO> getEntertaintmentById(@Parameter(description = "ID of the Entertaintment to retrieve") @PathVariable Long id) {
        EntertaintmentResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Entertaintment", description = "Create a new Entertaintment entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Entertaintment"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<EntertaintmentResponseDTO> createEntertaintment(@Parameter(description = "Entertaintment data to create") @RequestBody EntertaintmentRequestDTO requestDTO) {
        EntertaintmentResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Entertaintment", description = "Update an existing Entertaintment entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Entertaintment"),
        @ApiResponse(responseCode = "404", description = "Entertaintment not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<EntertaintmentResponseDTO> updateEntertaintment(@Parameter(description = "ID of the Entertaintment to update") @PathVariable Long id, @Parameter(description = "Updated Entertaintment data") @RequestBody EntertaintmentRequestDTO requestDTO) {
        EntertaintmentResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Entertaintment", description = "Delete a Entertaintment entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted Entertaintment"),
        @ApiResponse(responseCode = "404", description = "Entertaintment not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteEntertaintment(@Parameter(description = "ID of the Entertaintment to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

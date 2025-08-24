package com.ijse.adlync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ijse.adlync.dto.request.ServicesRequestDTO;
import com.ijse.adlync.dto.response.ServicesResponseDTO;
import com.ijse.adlync.service.ServicesServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/servicess")
@Tag(name = "ServicesEntity Management", description = "APIs for managing ServicesEntity entities")
public class ServicesController {

    @Autowired
    private ServicesServiceImpl service;

    @GetMapping
    @Operation(summary = "Get all Servicess", description = "Retrieve a list of all Services entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Servicess"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ServicesResponseDTO>> getAllServicess() {
        List<ServicesResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Services by ID", description = "Retrieve a Services entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Services"),
        @ApiResponse(responseCode = "404", description = "Services not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ServicesResponseDTO> getServicesById(@Parameter(description = "ID of the Services to retrieve") @PathVariable Long id) {
        ServicesResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Services", description = "Create a new Services entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Services"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ServicesResponseDTO> createServices(@Parameter(description = "Services data to create") @RequestBody ServicesRequestDTO requestDTO) {
        ServicesResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Services", description = "Update an existing Services entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Services"),
        @ApiResponse(responseCode = "404", description = "Services not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ServicesResponseDTO> updateServices(@Parameter(description = "ID of the Services to update") @PathVariable Long id, @Parameter(description = "Updated Services data") @RequestBody ServicesRequestDTO requestDTO) {
        ServicesResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Services", description = "Delete a Services entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted Services"),
        @ApiResponse(responseCode = "404", description = "Services not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteServices(@Parameter(description = "ID of the Services to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

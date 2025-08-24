package com.ijse.adlync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ijse.adlync.dto.request.VehicleRequestDTO;
import com.ijse.adlync.dto.response.VehicleResponseDTO;
import com.ijse.adlync.service.VehicleServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "VehicleEntity Management", description = "APIs for managing VehicleEntity entities")
public class VehicleController {

    @Autowired
    private VehicleServiceImpl service;

    @GetMapping
    @Operation(summary = "Get all Vehicles", description = "Retrieve a list of all Vehicle entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Vehicles"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<VehicleResponseDTO>> getAllVehicles() {
        List<VehicleResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Vehicle by ID", description = "Retrieve a Vehicle entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Vehicle"),
        @ApiResponse(responseCode = "404", description = "Vehicle not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<VehicleResponseDTO> getVehicleById(@Parameter(description = "ID of the Vehicle to retrieve") @PathVariable Long id) {
        VehicleResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Vehicle", description = "Create a new Vehicle entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Vehicle"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<VehicleResponseDTO> createVehicle(@Parameter(description = "Vehicle data to create") @RequestBody VehicleRequestDTO requestDTO) {
        VehicleResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Vehicle", description = "Update an existing Vehicle entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Vehicle"),
        @ApiResponse(responseCode = "404", description = "Vehicle not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<VehicleResponseDTO> updateVehicle(@Parameter(description = "ID of the Vehicle to update") @PathVariable Long id, @Parameter(description = "Updated Vehicle data") @RequestBody VehicleRequestDTO requestDTO) {
        VehicleResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Vehicle", description = "Delete a Vehicle entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted Vehicle"),
        @ApiResponse(responseCode = "404", description = "Vehicle not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteVehicle(@Parameter(description = "ID of the Vehicle to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

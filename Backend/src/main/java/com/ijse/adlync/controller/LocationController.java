package com.ijse.adlync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ijse.adlync.dto.request.LocationRequestDTO;
import com.ijse.adlync.dto.response.LocationResponseDTO;
import com.ijse.adlync.service.impl.LocationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/locations")
@Tag(name = "LocationEntity Management", description = "APIs for managing LocationEntity entities")
public class LocationController {

    @Autowired
    private LocationServiceImpl service;

    @GetMapping
    @Operation(summary = "Get all Locations", description = "Retrieve a list of all Location entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Locations"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<LocationResponseDTO>> getAllLocations() {
        List<LocationResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Location by ID", description = "Retrieve a Location entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Location"),
        @ApiResponse(responseCode = "404", description = "Location not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LocationResponseDTO> getLocationById(@Parameter(description = "ID of the Location to retrieve") @PathVariable Long id) {
        LocationResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Location", description = "Create a new Location entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Location"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LocationResponseDTO> createLocation(@Parameter(description = "Location data to create") @RequestBody LocationRequestDTO requestDTO) {
        LocationResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Location", description = "Update an existing Location entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Location"),
        @ApiResponse(responseCode = "404", description = "Location not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LocationResponseDTO> updateLocation(@Parameter(description = "ID of the Location to update") @PathVariable Long id, @Parameter(description = "Updated Location data") @RequestBody LocationRequestDTO requestDTO) {
        LocationResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Location", description = "Delete a Location entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted Location"),
        @ApiResponse(responseCode = "404", description = "Location not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteLocation(@Parameter(description = "ID of the Location to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

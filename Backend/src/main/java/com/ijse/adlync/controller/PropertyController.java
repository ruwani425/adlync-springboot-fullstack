package com.ijse.adlync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ijse.adlync.dto.request.PropertyRequestDTO;
import com.ijse.adlync.dto.response.PropertyResponseDTO;
import com.ijse.adlync.service.PropertyServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/propertys")
@Tag(name = "PropertyEntity Management", description = "APIs for managing PropertyEntity entities")
public class PropertyController {

    @Autowired
    private PropertyServiceImpl service;

    @GetMapping
    @Operation(summary = "Get all Propertys", description = "Retrieve a list of all Property entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Propertys"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PropertyResponseDTO>> getAllPropertys() {
        List<PropertyResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Property by ID", description = "Retrieve a Property entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Property"),
        @ApiResponse(responseCode = "404", description = "Property not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PropertyResponseDTO> getPropertyById(@Parameter(description = "ID of the Property to retrieve") @PathVariable Long id) {
        PropertyResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Property", description = "Create a new Property entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Property"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PropertyResponseDTO> createProperty(@Parameter(description = "Property data to create") @RequestBody PropertyRequestDTO requestDTO) {
        PropertyResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Property", description = "Update an existing Property entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Property"),
        @ApiResponse(responseCode = "404", description = "Property not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PropertyResponseDTO> updateProperty(@Parameter(description = "ID of the Property to update") @PathVariable Long id, @Parameter(description = "Updated Property data") @RequestBody PropertyRequestDTO requestDTO) {
        PropertyResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Property", description = "Delete a Property entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted Property"),
        @ApiResponse(responseCode = "404", description = "Property not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteProperty(@Parameter(description = "ID of the Property to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

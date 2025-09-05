package com.ijse.adlync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ijse.adlync.dto.request.Home_and_gardenRequestDTO;
import com.ijse.adlync.dto.response.Home_and_gardenResponseDTO;
import com.ijse.adlync.service.impl.Home_and_gardenServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/home_and_gardens")
@Tag(name = "Home_and_gardenEntity Management", description = "APIs for managing Home_and_gardenEntity entities")
public class Home_and_gardenController {

    @Autowired
    private Home_and_gardenServiceImpl service;

    @GetMapping
    @Operation(summary = "Get all Home_and_gardens", description = "Retrieve a list of all Home_and_garden entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Home_and_gardens"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Home_and_gardenResponseDTO>> getAllHome_and_gardens() {
        List<Home_and_gardenResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Home_and_garden by ID", description = "Retrieve a Home_and_garden entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Home_and_garden"),
        @ApiResponse(responseCode = "404", description = "Home_and_garden not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Home_and_gardenResponseDTO> getHome_and_gardenById(@Parameter(description = "ID of the Home_and_garden to retrieve") @PathVariable Long id) {
        Home_and_gardenResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Home_and_garden", description = "Create a new Home_and_garden entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Home_and_garden"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Home_and_gardenResponseDTO> createHome_and_garden(@Parameter(description = "Home_and_garden data to create") @RequestBody Home_and_gardenRequestDTO requestDTO) {
        Home_and_gardenResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Home_and_garden", description = "Update an existing Home_and_garden entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Home_and_garden"),
        @ApiResponse(responseCode = "404", description = "Home_and_garden not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Home_and_gardenResponseDTO> updateHome_and_garden(@Parameter(description = "ID of the Home_and_garden to update") @PathVariable Long id, @Parameter(description = "Updated Home_and_garden data") @RequestBody Home_and_gardenRequestDTO requestDTO) {
        Home_and_gardenResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Home_and_garden", description = "Delete a Home_and_garden entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted Home_and_garden"),
        @ApiResponse(responseCode = "404", description = "Home_and_garden not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteHome_and_garden(@Parameter(description = "ID of the Home_and_garden to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

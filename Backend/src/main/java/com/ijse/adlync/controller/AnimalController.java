package com.ijse.adlync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ijse.adlync.dto.request.AnimalRequestDTO;
import com.ijse.adlync.dto.response.AnimalResponseDTO;
import com.ijse.adlync.service.AnimalServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/animals")
@Tag(name = "AnimalEntity Management", description = "APIs for managing AnimalEntity entities")
public class AnimalController {

    @Autowired
    private AnimalServiceImpl service;

    @GetMapping
    @Operation(summary = "Get all Animals", description = "Retrieve a list of all Animal entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Animals"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<AnimalResponseDTO>> getAllAnimals() {
        List<AnimalResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Animal by ID", description = "Retrieve a Animal entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Animal"),
        @ApiResponse(responseCode = "404", description = "Animal not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AnimalResponseDTO> getAnimalById(@Parameter(description = "ID of the Animal to retrieve") @PathVariable Long id) {
        AnimalResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Animal", description = "Create a new Animal entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Animal"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AnimalResponseDTO> createAnimal(@Parameter(description = "Animal data to create") @RequestBody AnimalRequestDTO requestDTO) {
        System.out.println(requestDTO.toString());
        AnimalResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Animal", description = "Update an existing Animal entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Animal"),
        @ApiResponse(responseCode = "404", description = "Animal not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AnimalResponseDTO> updateAnimal(@Parameter(description = "ID of the Animal to update") @PathVariable Long id, @Parameter(description = "Updated Animal data") @RequestBody AnimalRequestDTO requestDTO) {
        AnimalResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Animal", description = "Delete a Animal entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted Animal"),
        @ApiResponse(responseCode = "404", description = "Animal not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteAnimal(@Parameter(description = "ID of the Animal to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

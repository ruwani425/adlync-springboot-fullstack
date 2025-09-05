package com.ijse.adlync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ijse.adlync.dto.request.MobileRequestDTO;
import com.ijse.adlync.dto.response.MobileResponseDTO;
import com.ijse.adlync.service.impl.MobileServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/mobiles")
@Tag(name = "MobileEntity Management", description = "APIs for managing MobileEntity entities")
public class MobileController {

    @Autowired
    private MobileServiceImpl service;

    @GetMapping
    @Operation(summary = "Get all Mobiles", description = "Retrieve a list of all Mobile entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Mobiles"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<MobileResponseDTO>> getAllMobiles() {
        List<MobileResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Mobile by ID", description = "Retrieve a Mobile entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Mobile"),
        @ApiResponse(responseCode = "404", description = "Mobile not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<MobileResponseDTO> getMobileById(@Parameter(description = "ID of the Mobile to retrieve") @PathVariable Long id) {
        MobileResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Mobile", description = "Create a new Mobile entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Mobile"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<MobileResponseDTO> createMobile(@Parameter(description = "Mobile data to create") @RequestBody MobileRequestDTO requestDTO) {
        MobileResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Mobile", description = "Update an existing Mobile entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Mobile"),
        @ApiResponse(responseCode = "404", description = "Mobile not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<MobileResponseDTO> updateMobile(@Parameter(description = "ID of the Mobile to update") @PathVariable Long id, @Parameter(description = "Updated Mobile data") @RequestBody MobileRequestDTO requestDTO) {
        MobileResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Mobile", description = "Delete a Mobile entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted Mobile"),
        @ApiResponse(responseCode = "404", description = "Mobile not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteMobile(@Parameter(description = "ID of the Mobile to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

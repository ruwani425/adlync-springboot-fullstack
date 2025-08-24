package com.ijse.adlync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ijse.adlync.dto.request.CategoryRequestDTO;
import com.ijse.adlync.dto.response.CategoryResponseDTO;
import com.ijse.adlync.service.CategoryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/categorys")
@Tag(name = "CategoryEntity Management", description = "APIs for managing CategoryEntity entities")
public class CategoryController {

    @Autowired
    private CategoryServiceImpl service;

    @GetMapping
    @Operation(summary = "Get all Categorys", description = "Retrieve a list of all Category entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Categorys"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategorys() {
        List<CategoryResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Category by ID", description = "Retrieve a Category entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Category"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@Parameter(description = "ID of the Category to retrieve") @PathVariable Long id) {
        CategoryResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Category", description = "Create a new Category entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Category"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CategoryResponseDTO> createCategory(@Parameter(description = "Category data to create") @RequestBody CategoryRequestDTO requestDTO) {
        CategoryResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Category", description = "Update an existing Category entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Category"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CategoryResponseDTO> updateCategory(@Parameter(description = "ID of the Category to update") @PathVariable Long id, @Parameter(description = "Updated Category data") @RequestBody CategoryRequestDTO requestDTO) {
        CategoryResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Category", description = "Delete a Category entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted Category"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteCategory(@Parameter(description = "ID of the Category to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

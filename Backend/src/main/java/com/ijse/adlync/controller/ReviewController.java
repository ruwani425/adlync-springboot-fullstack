package com.ijse.adlync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ijse.adlync.dto.request.ReviewRequestDTO;
import com.ijse.adlync.dto.response.ReviewResponseDTO;
import com.ijse.adlync.service.impl.ReviewServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "ReviewEntity Management", description = "APIs for managing ReviewEntity entities")
public class ReviewController {

    @Autowired
    private ReviewServiceImpl service;

    @GetMapping
    @Operation(summary = "Get all Reviews", description = "Retrieve a list of all Review entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Reviews"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ReviewResponseDTO>> getAllReviews() {
        List<ReviewResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Review by ID", description = "Retrieve a Review entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Review"),
        @ApiResponse(responseCode = "404", description = "Review not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ReviewResponseDTO> getReviewById(@Parameter(description = "ID of the Review to retrieve") @PathVariable Long id) {
        ReviewResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Review", description = "Create a new Review entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Review"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ReviewResponseDTO> createReview(@Parameter(description = "Review data to create") @RequestBody ReviewRequestDTO requestDTO) {
        ReviewResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Review", description = "Update an existing Review entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Review"),
        @ApiResponse(responseCode = "404", description = "Review not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ReviewResponseDTO> updateReview(@Parameter(description = "ID of the Review to update") @PathVariable Long id, @Parameter(description = "Updated Review data") @RequestBody ReviewRequestDTO requestDTO) {
        ReviewResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Review", description = "Delete a Review entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted Review"),
        @ApiResponse(responseCode = "404", description = "Review not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteReview(@Parameter(description = "ID of the Review to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

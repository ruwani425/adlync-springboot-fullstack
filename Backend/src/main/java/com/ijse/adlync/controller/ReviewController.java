package com.ijse.adlync.controller;

import com.ijse.adlync.dto.response.ReviewStatsDTO;
import com.ijse.adlync.entity.UserEntity;
import com.ijse.adlync.repository.UserRepository;
import com.ijse.adlync.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/post/{postId}")
    @Operation(summary = "Get reviews by post ID", description = "Retrieve reviews for a specific post with optional limit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved reviews"),
            @ApiResponse(responseCode = "404", description = "Post not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByPostId(
            @Parameter(description = "ID of the post") @PathVariable Long postId,
            @Parameter(description = "Limit number of reviews (optional)") @RequestParam(required = false) Integer limit) {
        List<ReviewResponseDTO> reviews = service.findByPostId(postId, limit);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/post/{postId}/stats")
    @Operation(summary = "Get review statistics for a post", description = "Get aggregated review statistics for a specific post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved review stats"),
            @ApiResponse(responseCode = "404", description = "Post not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ReviewStatsDTO> getReviewStatsByPostId(
            @Parameter(description = "ID of the post") @PathVariable Long postId) {
        ReviewStatsDTO stats = service.getReviewStatsByPostId(postId);
        return ResponseEntity.ok(stats);
    }

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
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ReviewResponseDTO> createReview(
            @Parameter(description = "Review data to create") @RequestBody ReviewRequestDTO requestDTO,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Validate token
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);

            // Fetch user
            UserEntity user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

            // Create review
            ReviewResponseDTO response = service.create(requestDTO, user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
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

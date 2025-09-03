package com.ijse.adlync.controller;

import com.ijse.adlync.dto.request.AnimalRequestDTO;
import com.ijse.adlync.dto.request.PostRequestDTO;
import com.ijse.adlync.dto.request.PropertyRequestDTO;
import com.ijse.adlync.dto.request.VehicleRequestDTO;
import com.ijse.adlync.dto.response.PostResponseDTO;
import com.ijse.adlync.service.PostServiceImpl;
import com.ijse.adlync.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "PostEntity Management", description = "APIs for managing PostEntity entities")
public class PostController {

    @Autowired
    private PostServiceImpl service;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    @Operation(summary = "Get all Posts", description = "Retrieve a list of all Post entities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Posts"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        List<PostResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Post by ID", description = "Retrieve a Post entity by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Post"),
            @ApiResponse(responseCode = "404", description = "Post not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PostResponseDTO> getPostById(@Parameter(description = "ID of the Post to retrieve") @PathVariable Long id) {
        PostResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Post", description = "Create a new Post entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created Post"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PostResponseDTO> createPost(@Parameter(description = "Post data to create") @RequestBody PostRequestDTO requestDTO) {
        PostResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Post", description = "Update an existing Post entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated Post"),
            @ApiResponse(responseCode = "404", description = "Post not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PostResponseDTO> updatePost(@Parameter(description = "ID of the Post to update") @PathVariable Long id, @Parameter(description = "Updated Post data") @RequestBody PostRequestDTO requestDTO) {
        PostResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Post", description = "Delete a Post entity by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted Post"),
            @ApiResponse(responseCode = "404", description = "Post not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deletePost(@Parameter(description = "ID of the Post to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/create-animal")
    @Operation(summary = "create post", description = "create a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created Post"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public String createAnimal(@Parameter(description = "Post data to create") @RequestBody AnimalRequestDTO requestDTO, @RequestHeader("Authorization") String authorizationHeader) {
        String username = "";
        if (authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(token);
        }
        System.out.println(requestDTO.toString());
        System.out.println(requestDTO.getPostRequestDTO().getDescription());
        return service.createAnimalPost(requestDTO, username);
//        return ResponseEntity.ok(response);
    }

    @PostMapping("/create-vehicle")
    @Operation(summary = "create post", description = "create a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created Post"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public String createVehicle(@Parameter(description = "Post data to create") @RequestBody VehicleRequestDTO requestDTO, @RequestHeader("Authorization") String authorizationHeader) {
        String username = "";
        if (authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(token);
        }
        System.out.println(requestDTO.toString());
        System.out.println(requestDTO.getPostRequestDTO().getDescription());
        return service.createVehiclePost(requestDTO, username);
    }

    @PostMapping("create-property")
    @Operation(summary = "create post", description = "create a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created Post"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public String createProperty(@Parameter(description = "Post data to create") @RequestBody PropertyRequestDTO requestDTO, @RequestHeader("Authorization") String authorizationHeader) {
        String username = "";
        if (authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(token);
        }
        System.out.println(requestDTO.toString());
        System.out.println(requestDTO.getPostRequestDTO().getDescription());
        return service.createPropertyPost(requestDTO,username);
    }
}

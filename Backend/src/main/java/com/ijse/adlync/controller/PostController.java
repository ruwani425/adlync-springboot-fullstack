package com.ijse.adlync.controller;

import com.ijse.adlync.dto.request.*;
import com.ijse.adlync.dto.response.PageResponse;
import com.ijse.adlync.dto.response.PostResponseDTO;
import com.ijse.adlync.entity.enums.PostEntityStatusEnum;
import com.ijse.adlync.service.PostService;
import com.ijse.adlync.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "PostEntity Management", description = "APIs for managing PostEntity entities")
public class PostController {

    private final PostService service;
    private final JwtUtil jwtUtil;

    @GetMapping
    @Operation(summary = "Get all Posts", description = "Retrieve a list of all Post entities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Posts"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Post by ID", description = "Retrieve a Post with full details")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        PostResponseDTO responseDTO = service.findById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/page")
    public ResponseEntity<PageResponse<PostResponseDTO>> getPostsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "PENDING") PostEntityStatusEnum status
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.findAllByStatus(status, pageable));
    }

    @PostMapping
    @Operation(summary = "Create new Post", description = "Create a new Post entity")
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody PostRequestDTO requestDTO) {
        return ResponseEntity.status(201).body(service.create(requestDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Post", description = "Update an existing Post entity")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long id,
                                                      @RequestBody PostRequestDTO requestDTO) {
        return ResponseEntity.ok(service.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Post", description = "Delete a Post entity by its ID")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/create-animal")
    @Operation(summary = "Create Animal Post", description = "Create a new Animal Post")
    public ResponseEntity<PostResponseDTO> createAnimal(@RequestBody AnimalRequestDTO requestDTO,
                                                        @RequestHeader("Authorization") String authorizationHeader) {
        String username = extractUsernameFromToken(authorizationHeader);
        return ResponseEntity.status(201).body(service.createAnimalPost(requestDTO, username));
    }

    @PostMapping("/create-vehicle")
    @Operation(summary = "Create Vehicle Post", description = "Create a new Vehicle Post")
    public ResponseEntity<PostResponseDTO> createVehicle(@RequestBody VehicleRequestDTO requestDTO,
                                                         @RequestHeader("Authorization") String authorizationHeader) {
        String username = extractUsernameFromToken(authorizationHeader);
        return ResponseEntity.status(201).body(service.createVehiclePost(requestDTO, username));
    }

    @PostMapping("/create-property")
    @Operation(summary = "Create Property Post", description = "Create a new Property Post")
    public ResponseEntity<PostResponseDTO> createProperty(@RequestBody PropertyRequestDTO requestDTO,
                                                          @RequestHeader("Authorization") String authorizationHeader) {
        String username = extractUsernameFromToken(authorizationHeader);
        return ResponseEntity.status(201).body(service.createPropertyPost(requestDTO, username));
    }

    @PostMapping("/create-agriculture")
    @Operation(summary = "Create Agriculture Post", description = "Create a new Agriculture Post")
    public ResponseEntity<PostResponseDTO> createAgriculturePost(@RequestBody AgricultureRequestDTO requestDTO,
                                                                 @RequestHeader("Authorization") String authorizationHeader) {
        String username = extractUsernameFromToken(authorizationHeader);
        return ResponseEntity.status(201).body(service.createAgriculturePost(requestDTO, username));
    }

    @PostMapping("/create-education")
    @Operation(summary = "Create Education Post", description = "Create a new Education Post")
    public ResponseEntity<PostResponseDTO> createEducationPost(@RequestBody EducationRequestDTO requestDTO,
                                                               @RequestHeader("Authorization") String authorizationHeader) {
        String username = extractUsernameFromToken(authorizationHeader);
        return ResponseEntity.status(201).body(service.createEducationPost(requestDTO, username));
    }

    @PostMapping("/create-electronic")
    @Operation(summary = "Create Electronic Post", description = "Create a new Electronic Post")
    public ResponseEntity<PostResponseDTO> createElectronicPost(@RequestBody ElectronicRequestDTO requestDTO, @RequestHeader("Authorization") String authorizationHeader) {
        String username = extractUsernameFromToken(authorizationHeader);
        return ResponseEntity.status(201).body(service.createElectronicPost(requestDTO, username));
    }

    @PostMapping("/create-entertainment")
    @Operation(summary = "Create Entertainment Post", description = "Create a new Entertainment Post")
    public ResponseEntity<PostResponseDTO> createEntertaintmentPOst(@RequestBody EntertaintmentRequestDTO requestDTO, @RequestHeader("Authorization") String authorizationHeader) {
        String username = extractUsernameFromToken(authorizationHeader);
        return ResponseEntity.status(201).body(service.createEntertaintmentPost(requestDTO, username));
    }

    @PostMapping("/create-essential")
    @Operation(summary = "Create Essential Post", description = "Create a new Essential Post")
    public ResponseEntity<PostResponseDTO> createEssentialPost(@RequestBody EssentialsRequestDTO requestDTO, @RequestHeader("Authorization") String authorizationHeader) {
        String username = extractUsernameFromToken(authorizationHeader);
        return ResponseEntity.status(201).body(service.createEssentialPost(requestDTO, username));
    }

    @PostMapping("/create-fashion-and-beauty")
    @Operation(summary = "Create New Fashion and Beauty Post", description = "Create a new Fashion and Beauty Post")
    public ResponseEntity<PostResponseDTO> createFashionAndBeautyPost(@RequestBody Fashion_and_beautyRequestDTO requestDTO, @RequestHeader("Authorization") String authorizationHeader) {
        String username = extractUsernameFromToken(authorizationHeader);
        return ResponseEntity.status(201).body(service.createFashionAndBeautyPost(requestDTO, username));
    }

    @PostMapping("/create-home-and-garden")
    @Operation(summary = "Create Home and Garden Post", description = "Create a new Home and Garden Post")
    public ResponseEntity<PostResponseDTO> createHomeAndGardenPost(@RequestBody Home_and_gardenRequestDTO requestDTO, @RequestHeader("Authorization") String authorizationHeader) {
        String username = extractUsernameFromToken(authorizationHeader);
        return ResponseEntity.status(201).body(service.createHomeAndGardenPost(requestDTO, username));
    }

    @PostMapping("/create-job")
    @Operation(summary = "Create Job Post", description = "Create a new Job Post")
    public ResponseEntity<PostResponseDTO> createJobPost(@RequestBody JobRequestDTO requestDTO, @RequestHeader("Authorization") String authorizationHeader) {
        String username = extractUsernameFromToken(authorizationHeader);
        return ResponseEntity.status(201).body(service.createJobPost(requestDTO, username));
    }

    @PostMapping("/create-kids")
    @Operation(summary = "Create Kids Post", description = "Create a new Kids Post")
    public ResponseEntity<PostResponseDTO> createKidsPost(@RequestBody KidsRequestDTO requestDTO, @RequestHeader("Authorization") String authorizationHeader) {
        String username = extractUsernameFromToken(authorizationHeader);
        return ResponseEntity.status(201).body(service.createKidsPost(requestDTO, username));
    }

    @PostMapping("/create-mobile")
    @Operation(summary = "Create Mobile Post", description = "Create a new Mobile Post")
    public ResponseEntity<PostResponseDTO> createMobilePost(@RequestBody MobileRequestDTO requestDTO, @RequestHeader("Authorization") String authorizationHeader) {
        String username = extractUsernameFromToken(authorizationHeader);
        return ResponseEntity.status(201).body(service.createMobilePost(requestDTO, username));
    }

    @PostMapping("/create-service")
    @Operation(summary = "Create Service Post", description = "Create a new Service Post")
    public ResponseEntity<PostResponseDTO> createServicesEntity(@RequestBody ServicesRequestDTO requestDTO, @RequestHeader("Authorization") String authorizationHeader) {
        String username = extractUsernameFromToken(authorizationHeader);
        return ResponseEntity.status(201).body(service.createServicePost(requestDTO, username));
    }

    @PostMapping("/create-sport")
    @Operation(summary = "Create Sport Post", description = "Create a new Sport Post")
    public ResponseEntity<PostResponseDTO> createSportPost(@RequestBody SportRequestDTO requestDTO, @RequestHeader("Authorization") String authorizationHeader) {
        String username = extractUsernameFromToken(authorizationHeader);
        return ResponseEntity.status(201).body(service.createSportPost(requestDTO, username));
    }

    @PostMapping("/create-work-over-sea")
    @Operation(summary = "Create Work Over Sea Post", description = "Create a new Work Over Sea Post")
    public ResponseEntity<PostResponseDTO> createWorkOverSeasPost(@RequestBody Work_over_seasRequestDTO requestDTO, @RequestHeader("Authorization") String authorizationHeader) {
        String username = extractUsernameFromToken(authorizationHeader);
        return ResponseEntity.status(201).body(service.createWorkOverSeaPost(requestDTO, username));
    }

    private String extractUsernameFromToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid Authorization header");
        }
        String token = authorizationHeader.substring(7);
        return jwtUtil.extractUsername(token);
    }
}

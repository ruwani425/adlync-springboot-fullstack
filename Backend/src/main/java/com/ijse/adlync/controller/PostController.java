package com.ijse.adlync.controller;

import com.ijse.adlync.dto.request.*;
import com.ijse.adlync.dto.response.PageResponse;
import com.ijse.adlync.dto.response.PostResponseDTO;
import com.ijse.adlync.entity.enums.CategoryEntityNameEnum;
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
import org.springframework.data.domain.Sort;
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

    @GetMapping("/page/advanced")
    @Operation(summary = "Advanced Filter Posts", description = "Filter posts with location, condition, price range, and other parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered posts"),
            @ApiResponse(responseCode = "400", description = "Invalid filter parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PageResponse<PostResponseDTO>> getAdvancedFilteredPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) PostEntityStatusEnum status,
            @RequestParam(required = false) CategoryEntityNameEnum category,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String sortBy
    ) {
        try {
            System.out.println("=== Advanced Filter Request ===");
            System.out.println("Page: " + page + ", Size: " + size);
            System.out.println("Status: " + status);
            System.out.println("Category: " + category);
            System.out.println("Search: " + search);
            System.out.println("Location: " + location);
            System.out.println("Condition: " + condition);
            System.out.println("MinPrice: " + minPrice);
            System.out.println("MaxPrice: " + maxPrice);
            System.out.println("SortBy: " + sortBy);

            if (page < 0) page = 0;
            if (size <= 0) size = 10;
            if (size > 100) size = 100;

            Pageable pageable;
            if (sortBy != null && !sortBy.trim().isEmpty()) {
                Sort sort = createSort(sortBy.trim());
                pageable = PageRequest.of(page, size, sort);
            } else {
                Sort defaultSort = Sort.by(Sort.Direction.DESC, "createdAt");
                pageable = PageRequest.of(page, size, defaultSort);
            }

            if (minPrice != null && minPrice < 0) minPrice = null;
            if (maxPrice != null && maxPrice < 0) maxPrice = null;

            if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
                Double temp = minPrice;
                minPrice = maxPrice;
                maxPrice = temp;
            }

            if (status == null) {
                status = PostEntityStatusEnum.APPROVED;
            }

            PageResponse<PostResponseDTO> response = service.advancedFilterPosts(
                    status, category, startDate, endDate, search,
                    location, condition, minPrice, maxPrice, pageable
            );

            System.out.println("Service returned " + response.getContent().size() + " results");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error in getAdvancedFilteredPosts: " + e.getMessage());
            e.printStackTrace();

            PageResponse<PostResponseDTO> emptyResponse = new PageResponse<>(
                    List.of(), 0, size, 0L, 0, true
            );
            return ResponseEntity.status(500).body(emptyResponse);
        }
    }

    private Sort createSort(String sortBy) {
        switch (sortBy.toLowerCase()) {
            case "price-low":
            case "price_low":
            case "price-asc":
                return Sort.by(Sort.Direction.ASC, "price");

            case "price-high":
            case "price_high":
            case "price-desc":
                return Sort.by(Sort.Direction.DESC, "price");

            case "oldest":
            case "date-asc":
                return Sort.by(Sort.Direction.ASC, "createdAt");

            case "popular":
            case "popularity":
                return Sort.by(Sort.Direction.DESC, "createdAt");

            case "newest":
            case "date-desc":
            default:
                return Sort.by(Sort.Direction.DESC, "createdAt");
        }
    }

    @GetMapping
    @Operation(summary = "Get all Posts", description = "Retrieve a list of all Post entities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Posts"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<PageResponse<PostResponseDTO>> getPostsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(required = false) String status
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<PostResponseDTO> response = service.findPostsByUserWithPagination(userId, status, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count/by-user/{userId}")
    public ResponseEntity<Long> getPostCountByUser(@PathVariable Long userId) {
        long count = service.getPostCountByUser(userId);
        System.out.println("\n\n\n\n" + "post count :" + count);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/post-detail/{id}")
    @Operation(summary = "Get Post by ID", description = "Retrieve a Post with full details")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        PostResponseDTO responseDTO = service.findById(id);
        System.out.println("post responce dto :" + responseDTO.toString());
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/page")
    @Operation(summary = "Filter Posts for Advertisement Page", description = "Filter posts by status, category, date range, and search")
    public ResponseEntity<PageResponse<PostResponseDTO>> getPostsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) PostEntityStatusEnum status,
            @RequestParam(required = false) CategoryEntityNameEnum category,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String search
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<PostResponseDTO> response = service.filterPostsForAds(
                status, category, startDate, endDate, search, pageable
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/approved/all")
    public ResponseEntity<PageResponse<PostResponseDTO>> getApprovedPostsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.findAllByStatus(PostEntityStatusEnum.APPROVED, pageable));
    }

    @GetMapping("/approved/recent")
    public ResponseEntity<PageResponse<PostResponseDTO>> getRecentApprovedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {

        Pageable pageable = PageRequest.of(page, size);
        PageResponse<PostResponseDTO> pageResponse = service.findAllByStatus(PostEntityStatusEnum.APPROVED, pageable);
        return ResponseEntity.ok(pageResponse);
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

    @PutMapping("/{id}/status/{status}")
    @Operation(summary = "Update Post Status", description = "Update post status (APPROVED / REJECTED / PENDING)")
    public ResponseEntity<PostResponseDTO> updatePostStatus(
            @PathVariable Long id,
            @PathVariable PostEntityStatusEnum status) {

        PostResponseDTO updatedPost = service.updatePostStatus(id, status);
        return ResponseEntity.ok(updatedPost);
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

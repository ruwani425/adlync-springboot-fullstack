package com.ijse.adlync.service.impl;

import com.ijse.adlync.dto.request.ReviewRequestDTO;
import com.ijse.adlync.dto.response.ReviewResponseDTO;
import com.ijse.adlync.dto.response.ReviewStatsDTO;
import com.ijse.adlync.entity.PostEntity;
import com.ijse.adlync.entity.ReviewEntity;
import com.ijse.adlync.entity.UserEntity;
import com.ijse.adlync.repository.PostRepository;
import com.ijse.adlync.repository.ReviewRepository;
import com.ijse.adlync.repository.UserRepository;
import com.ijse.adlync.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository repository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ReviewResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ReviewResponseDTO findById(Long id) {
        ReviewEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReviewEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public ReviewResponseDTO create(ReviewRequestDTO requestDTO, UserEntity user) {
        ReviewEntity entity = toEntity(requestDTO, user);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public ReviewResponseDTO update(Long id, ReviewRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("ReviewEntity not found with id: " + id);
        }
        ReviewEntity entity = toEntity(requestDTO, null);
        entity.setReview_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("ReviewEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private ReviewResponseDTO toResponseDTO(ReviewEntity entity) {
        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setReview_id(entity.getReview_id());
        dto.setRating(entity.getRating());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setQualityRating(entity.getQualityRating());
        dto.setCommunicationRating(entity.getCommunicationRating());
        dto.setValueRating(entity.getValueRating());
        dto.setDeliveryRating(entity.getDeliveryRating());
        dto.setRecommendation(entity.getRecommendation());
        dto.setAnonymous(entity.getAnonymous());
        dto.setVerified(entity.getVerified());
        dto.setCreated_at(entity.getCreated_at());

        if (entity.getAnonymous()) {
            dto.setReviewerName("Anonymous");
        } else if (entity.getUser() != null) {
            dto.setReviewerName(entity.getUser().getName());
        }

        if (entity.getPost() != null) {
            dto.setPostId(entity.getPost().getPost_id());
        }
        System.out.println("\n\n\nto responseDTO" + dto.toString());
        return dto;
    }

    private ReviewEntity toEntity(ReviewRequestDTO dto, UserEntity user) {
        ReviewEntity entity = new ReviewEntity();
        entity.setRating(dto.getRating());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setQualityRating(dto.getQualityRating());
        entity.setCommunicationRating(dto.getCommunicationRating());
        entity.setValueRating(dto.getValueRating());
        entity.setDeliveryRating(dto.getDeliveryRating());
        entity.setRecommendation(dto.getRecommendation());
        entity.setAnonymous(dto.getAnonymous() != null ? dto.getAnonymous() : false);
        entity.setVerified(dto.getVerified() != null ? dto.getVerified() : false);
        entity.setAspects(dto.getAspects());


        if (user != null) {
            entity.setUser(user);
        } else {
            throw new RuntimeException("User is required for review creation");
        }

        if (dto.getPostId() != null) {
            PostEntity post = postRepository.findById(dto.getPostId())
                    .orElseThrow(() -> new RuntimeException("Post not found with id: " + dto.getPostId()));
            entity.setPost(post);
        } else {
            throw new RuntimeException("postId is required for review creation");
        }
        System.out.println("\n\nto entity: " + entity);
        return entity;
    }


    public List<ReviewResponseDTO> findByPostId(Long postId, Integer limit) {
        PostEntity post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        List<ReviewEntity> reviews;

        if (limit != null && limit > 0) {
            PageRequest pageRequest = PageRequest.of(0, limit, Sort.by("created_at").descending());
            reviews = repository.findByPostIdWithLimit(postId, pageRequest);
        } else {
            reviews = repository.findByPost_OrderByCreated_atDesc(post);
        }
        System.out.println("\n\n" + reviews.stream().map(ReviewEntity::getReview_id).collect(Collectors.toList()) + "find by post method");

        return reviews.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ReviewStatsDTO getReviewStatsByPostId(Long postId) {
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        ReviewStatsDTO stats = new ReviewStatsDTO();

        Long totalReviews = repository.countByPost_Post_id(postEntity);
        stats.setTotalReviews(totalReviews);

        if (totalReviews == 0) {
            stats.setAverageRating(0.0);
            stats.setFiveStar(0L);
            stats.setFourStar(0L);
            stats.setThreeStar(0L);
            stats.setTwoStar(0L);
            stats.setOneStar(0L);
            stats.setRecommended(0L);
            stats.setVerifiedCount(0L);
            stats.setSellerRating(0.0);
            return stats;
        }

        Double avgRating = repository.findAverageRatingByPostId(postId);
        stats.setAverageRating(avgRating != null ? avgRating : 0.0);

        List<Object[]> ratingDistribution = repository.findRatingDistributionByPostId(postId);
        Map<Integer, Long> ratingCounts = new HashMap<>();

        for (Object[] row : ratingDistribution) {
            Double rating = (Double) row[0];
            Long count = (Long) row[1];
            if (rating != null) {
                ratingCounts.put(rating.intValue(), count);
            }
        }

        stats.setFiveStar(ratingCounts.getOrDefault(5, 0L));
        stats.setFourStar(ratingCounts.getOrDefault(4, 0L));
        stats.setThreeStar(ratingCounts.getOrDefault(3, 0L));
        stats.setTwoStar(ratingCounts.getOrDefault(2, 0L));
        stats.setOneStar(ratingCounts.getOrDefault(1, 0L));

        Long recommendedCount = repository.countRecommendedByPostId(postId);
        Long recommendedPercentage = totalReviews > 0 ? (recommendedCount * 100) / totalReviews : 0L;
        stats.setRecommended(recommendedPercentage);

        Long verifiedCount = repository.countVerifiedByPostId(postId);
        stats.setVerifiedCount(verifiedCount);

        stats.setSellerRating(avgRating != null ? avgRating : 0.0);
        System.out.println("\nstats: " + stats);
        return stats;
    }

}

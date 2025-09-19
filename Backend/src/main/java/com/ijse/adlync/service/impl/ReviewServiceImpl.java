package com.ijse.adlync.service.impl;

import com.ijse.adlync.dto.request.ReviewRequestDTO;
import com.ijse.adlync.dto.response.ReviewResponseDTO;
import com.ijse.adlync.entity.PostEntity;
import com.ijse.adlync.entity.ReviewEntity;
import com.ijse.adlync.entity.UserEntity;
import com.ijse.adlync.repository.PostRepository;
import com.ijse.adlync.repository.ReviewRepository;
import com.ijse.adlync.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl {

    @Autowired
    private ReviewRepository repository;

    @Autowired
    private PostRepository postRepository; // NEW: For post lookup

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
        ReviewEntity entity = toEntity(requestDTO, null); // User not updated
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

        // Set reviewer name based on anonymity
        if (entity.getAnonymous()) {
            dto.setReviewerName("Anonymous");
        } else if (entity.getUser() != null) {
            dto.setReviewerName(entity.getUser().getName());
            // No userId in response
        }

        // Set post ID
        if (entity.getPost() != null) {
            dto.setPostId(entity.getPost().getPost_id());
        }

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

        // Set user
        if (user != null) {
            entity.setUser(user);
        } else {
            throw new RuntimeException("User is required for review creation");
        }

        // Set post
        if (dto.getPostId() != null) {
            PostEntity post = postRepository.findById(dto.getPostId())
                    .orElseThrow(() -> new RuntimeException("Post not found with id: " + dto.getPostId()));
            entity.setPost(post);
        } else {
            throw new RuntimeException("postId is required for review creation");
        }

        return entity;
    }

}

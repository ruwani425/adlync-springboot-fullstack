package com.ijse.adlync.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.ijse.adlync.entity.ReviewEntity;
import com.ijse.adlync.repository.ReviewRepository;
import com.ijse.adlync.dto.request.ReviewRequestDTO;
import com.ijse.adlync.dto.response.ReviewResponseDTO;

@Service
public class ReviewServiceImpl {

    @Autowired
    private ReviewRepository repository;

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

    public ReviewResponseDTO create(ReviewRequestDTO requestDTO) {
        ReviewEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public ReviewResponseDTO update(Long id, ReviewRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("ReviewEntity not found with id: " + id);
        }
        ReviewEntity entity = toEntity(requestDTO);
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
        dto.setCreated_at(entity.getCreated_at());
        return dto;
    }

    private ReviewEntity toEntity(ReviewRequestDTO dto) {
        ReviewEntity entity = new ReviewEntity();
        entity.setRating(dto.getRating());
        entity.setCreated_at(dto.getCreated_at());
        return entity;
    }
}

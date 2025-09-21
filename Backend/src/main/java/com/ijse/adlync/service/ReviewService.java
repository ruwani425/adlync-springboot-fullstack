package com.ijse.adlync.service;

import com.ijse.adlync.dto.request.ReviewRequestDTO;
import com.ijse.adlync.dto.response.ReviewResponseDTO;
import com.ijse.adlync.dto.response.ReviewStatsDTO;
import com.ijse.adlync.entity.UserEntity;

import java.util.List;

public interface ReviewService {

    List<ReviewResponseDTO> findAll();

    ReviewResponseDTO findById(Long id);

    ReviewResponseDTO create(ReviewRequestDTO requestDTO, UserEntity user);

    ReviewResponseDTO update(Long id, ReviewRequestDTO requestDTO);

    void deleteById(Long id);

    List<ReviewResponseDTO> findByPostId(Long postId, Integer limit);

    ReviewStatsDTO getReviewStatsByPostId(Long postId);
}

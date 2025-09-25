package com.ijse.adlync.dto.response;

import com.ijse.adlync.entity.enums.RecommendationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDTO {
    private Long review_id;
    private Double rating;
    private String title;
    private String content;

    private Integer qualityRating;
    private Integer communicationRating;
    private Integer valueRating;
    private Integer deliveryRating;

    private RecommendationStatus recommendation;
    private Boolean anonymous;
    private Boolean verified;
    private LocalDateTime created_at;

    private String reviewerName;
    private Long userId;
    private Long postId;
}

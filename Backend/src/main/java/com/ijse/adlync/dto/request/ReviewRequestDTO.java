package com.ijse.adlync.dto.request;

import com.ijse.adlync.entity.enums.RecommendationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewRequestDTO {

    private Double rating;
    private LocalDateTime created_at;
    private Long review_id;
    private String title;
    private String content;

    // Aspect ratings
    private Integer qualityRating;
    private Integer communicationRating;
    private Integer valueRating;
    private Integer deliveryRating;

    private RecommendationStatus recommendation;
    private Boolean anonymous;
    private Boolean verified;
    private String aspects; // JSON string

    // User info
    private String reviewerName;
    private Long userId;

    private Long postId;
}

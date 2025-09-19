package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.RecommendationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long review_id;

    private Double rating;//1-5 stars

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(length = 500)
    private String aspects; // JSON string of checked aspects

    @Enumerated(EnumType.STRING)
    private RecommendationStatus recommendation;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private Boolean verified = false;// Whether this is a verified purchase/interaction

    private Integer qualityRating;        // Product Quality rating (1-5)
    private Integer communicationRating;  // Communication rating (1-5)
    private Integer valueRating;          // Value for Money rating (1-5)
    private Integer deliveryRating;       // Delivery/Pickup rating (1-5)


    @Column(nullable = false)
    private Boolean anonymous = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;
}

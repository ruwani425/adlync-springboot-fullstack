package com.ijse.adlync.repository;

import com.ijse.adlync.entity.PostEntity;
import com.ijse.adlync.entity.ReviewEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    @Query("SELECT r FROM ReviewEntity r WHERE r.post = :post ORDER BY r.created_at DESC")
    List<ReviewEntity> findByPost_OrderByCreated_atDesc(@Param("post") PostEntity post);

    @Query("SELECT r FROM ReviewEntity r WHERE r.post.post_id = :postId ORDER BY r.created_at DESC")
    List<ReviewEntity> findByPostIdWithLimit(@Param("postId") Long postId, Pageable pageable);

    @Query("SELECT COUNT(r) FROM ReviewEntity r WHERE r.post = :post")
    Long countByPost_Post_id(@Param("post") PostEntity post);

    @Query("SELECT AVG(r.rating) FROM ReviewEntity r WHERE r.post.post_id = :postId")
    Double findAverageRatingByPostId(@Param("postId") Long postId);

    @Query("SELECT r.rating, COUNT(r) FROM ReviewEntity r WHERE r.post.post_id = :postId GROUP BY r.rating")
    List<Object[]> findRatingDistributionByPostId(@Param("postId") Long postId);

    @Query("SELECT COUNT(r) FROM ReviewEntity r WHERE r.post.post_id = :postId AND r.recommendation = 'YES'")
    Long countRecommendedByPostId(@Param("postId") Long postId);

    @Query("SELECT COUNT(r) FROM ReviewEntity r WHERE r.post.post_id = :postId AND r.verified = true")
    Long countVerifiedByPostId(@Param("postId") Long postId);
}

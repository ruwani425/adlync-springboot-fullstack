package com.ijse.adlync.repository;

import com.ijse.adlync.entity.PostEntity;
import com.ijse.adlync.entity.enums.CategoryEntityNameEnum;
import com.ijse.adlync.entity.enums.PostEntityStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    Page<PostEntity> findAllByStatus(PostEntityStatusEnum status, Pageable pageable);

    long countByUser_Id(Long userId);

    @Query("""
                SELECT p FROM PostEntity p
                WHERE (:status IS NULL OR p.status = :status)
                  AND (:category IS NULL OR p.category.name = :category)
                  AND (:search IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%'))
                      OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')))
                  AND (:startDate IS NULL OR p.createdAt >= :startDate)
                  AND (:endDate IS NULL OR p.createdAt <= :endDate)
                ORDER BY p.createdAt DESC
            """)
    Page<PostEntity> findFilteredPosts(
            @Param("status") PostEntityStatusEnum status,
            @Param("category") CategoryEntityNameEnum category,
            @Param("search") String search,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

}

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
import java.util.List;

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

    List<PostEntity> findPostEntitYByUser_id(Long userId);

    Page<PostEntity> findByUser_idAndStatus(Long userId, PostEntityStatusEnum status, Pageable pageable);

    Page<PostEntity> findByUser_Id(Long userId, Pageable pageable);

    @Query("""
            SELECT DISTINCT p FROM PostEntity p
            LEFT JOIN p.location l
            LEFT JOIN p.vehicle v
            LEFT JOIN p.electronic e
            LEFT JOIN p.mobile m
            LEFT JOIN p.agriculture a
            LEFT JOIN p.entertainment ent
            LEFT JOIN p.essentials es
            LEFT JOIN p.fashion_and_beauty f
            LEFT JOIN p.home_and_garden h
            LEFT JOIN p.kids k
            LEFT JOIN p.sport s
            WHERE (:status IS NULL OR p.status = :status)
              AND (:category IS NULL OR p.category.name = :category)
              AND (:search IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%'))
                  OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')))
              AND (:startDate IS NULL OR p.createdAt >= :startDate)
              AND (:endDate IS NULL OR p.createdAt <= :endDate)
              AND (:location IS NULL OR LOWER(l.city) LIKE LOWER(CONCAT('%', :location, '%'))
                  OR LOWER(l.district) LIKE LOWER(CONCAT('%', :location, '%'))
                  OR LOWER(l.address) LIKE LOWER(CONCAT('%', :location, '%')))
              AND (:condition IS NULL OR 
                  LOWER(v.condition) LIKE LOWER(CONCAT('%', :condition, '%')) OR
                  LOWER(e.condition) LIKE LOWER(CONCAT('%', :condition, '%')) OR
                  LOWER(m.condition) LIKE LOWER(CONCAT('%', :condition, '%')) OR
                  LOWER(a.condition) LIKE LOWER(CONCAT('%', :condition, '%')) OR
                  LOWER(ent.condition) LIKE LOWER(CONCAT('%', :condition, '%')) OR
                  LOWER(es.condition) LIKE LOWER(CONCAT('%', :condition, '%')) OR
                  LOWER(f.condition) LIKE LOWER(CONCAT('%', :condition, '%')) OR
                  LOWER(h.condition) LIKE LOWER(CONCAT('%', :condition, '%')) OR
                  LOWER(k.condition) LIKE LOWER(CONCAT('%', :condition, '%')) OR
                  LOWER(s.condition) LIKE LOWER(CONCAT('%', :condition, '%')))
              AND (:minPrice IS NULL OR p.price >= :minPrice)
              AND (:maxPrice IS NULL OR p.price <= :maxPrice)
            """)
    Page<PostEntity> findAdvancedFilteredPosts(
            @Param("status") PostEntityStatusEnum status,
            @Param("category") CategoryEntityNameEnum category,
            @Param("search") String search,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("location") String location,
            @Param("condition") String condition,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable
    );

}

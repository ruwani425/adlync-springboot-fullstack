package com.ijse.adlync.repository;

import com.ijse.adlync.entity.PostEntity;
import com.ijse.adlync.entity.enums.PostEntityStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    Page<PostEntity> findAllByStatus(PostEntityStatusEnum status, Pageable pageable);

    long countByUser_Id(Long userId);
}

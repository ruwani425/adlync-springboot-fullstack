package com.ijse.adlync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ijse.adlync.entity.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    // Add custom query methods here
}

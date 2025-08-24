package com.ijse.adlync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ijse.adlync.entity.Advertisement_typeEntity;

@Repository
public interface Advertisement_typeRepository extends JpaRepository<Advertisement_typeEntity, Long> {
    // Add custom query methods here
}

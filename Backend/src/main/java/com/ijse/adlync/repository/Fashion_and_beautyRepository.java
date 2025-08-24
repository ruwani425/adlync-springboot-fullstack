package com.ijse.adlync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ijse.adlync.entity.Fashion_and_beautyEntity;

@Repository
public interface Fashion_and_beautyRepository extends JpaRepository<Fashion_and_beautyEntity, Long> {
    // Add custom query methods here
}

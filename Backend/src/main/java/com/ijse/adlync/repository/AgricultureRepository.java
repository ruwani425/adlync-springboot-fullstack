package com.ijse.adlync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ijse.adlync.entity.AgricultureEntity;

@Repository
public interface AgricultureRepository extends JpaRepository<AgricultureEntity, Long> {
    // Add custom query methods here
}

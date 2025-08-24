package com.ijse.adlync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ijse.adlync.entity.EntertaintmentEntity;

@Repository
public interface EntertaintmentRepository extends JpaRepository<EntertaintmentEntity, Long> {
    // Add custom query methods here
}

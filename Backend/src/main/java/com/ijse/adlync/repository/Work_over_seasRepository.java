package com.ijse.adlync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ijse.adlync.entity.Work_over_seasEntity;

@Repository
public interface Work_over_seasRepository extends JpaRepository<Work_over_seasEntity, Long> {
    // Add custom query methods here
}

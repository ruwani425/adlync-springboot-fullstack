package com.ijse.adlync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ijse.adlync.entity.ElectronicEntity;

@Repository
public interface ElectronicRepository extends JpaRepository<ElectronicEntity, Long> {
    // Add custom query methods here
}

package com.ijse.adlync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ijse.adlync.entity.EducationEntity;

@Repository
public interface EducationRepository extends JpaRepository<EducationEntity, Long> {
    // Add custom query methods here
}

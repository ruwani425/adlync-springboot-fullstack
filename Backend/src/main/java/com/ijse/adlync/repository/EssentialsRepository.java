package com.ijse.adlync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ijse.adlync.entity.EssentialsEntity;

@Repository
public interface EssentialsRepository extends JpaRepository<EssentialsEntity, Long> {
    // Add custom query methods here
}

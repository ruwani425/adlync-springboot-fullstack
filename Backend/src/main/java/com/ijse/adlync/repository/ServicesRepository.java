package com.ijse.adlync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ijse.adlync.entity.ServicesEntity;

@Repository
public interface ServicesRepository extends JpaRepository<ServicesEntity, Long> {
    // Add custom query methods here
}

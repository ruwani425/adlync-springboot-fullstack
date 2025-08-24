package com.ijse.adlync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ijse.adlync.entity.Home_and_gardenEntity;

@Repository
public interface Home_and_gardenRepository extends JpaRepository<Home_and_gardenEntity, Long> {
    // Add custom query methods here
}

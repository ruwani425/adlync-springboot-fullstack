package com.ijse.adlync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ijse.adlync.entity.MobileEntity;

@Repository
public interface MobileRepository extends JpaRepository<MobileEntity, Long> {
}

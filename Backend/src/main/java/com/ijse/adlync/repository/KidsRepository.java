package com.ijse.adlync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ijse.adlync.entity.KidsEntity;

@Repository
public interface KidsRepository extends JpaRepository<KidsEntity, Long> {
}

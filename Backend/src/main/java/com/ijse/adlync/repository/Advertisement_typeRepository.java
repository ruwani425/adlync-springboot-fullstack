package com.ijse.adlync.repository;

import com.ijse.adlync.entity.enums.Advertisement_typeEntityTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ijse.adlync.entity.Advertisement_typeEntity;

@Repository
public interface Advertisement_typeRepository extends JpaRepository<Advertisement_typeEntity, Long> {
    Advertisement_typeEntity findByType(Advertisement_typeEntityTypeEnum type);
}

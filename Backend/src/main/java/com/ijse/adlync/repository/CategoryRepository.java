package com.ijse.adlync.repository;

import com.ijse.adlync.entity.enums.CategoryEntityNameEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ijse.adlync.entity.CategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    CategoryEntity findByName(CategoryEntityNameEnum name);
}

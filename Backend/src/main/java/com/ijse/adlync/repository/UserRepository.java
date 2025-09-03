package com.ijse.adlync.repository;

import com.ijse.adlync.entity.enums.UserEntityRoleEnum;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ijse.adlync.entity.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    boolean existsByRole(UserEntityRoleEnum role);

    Optional<UserEntity> findByEmail(String email);
}

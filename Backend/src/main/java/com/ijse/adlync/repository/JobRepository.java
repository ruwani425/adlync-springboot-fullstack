package com.ijse.adlync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ijse.adlync.entity.JobEntity;

@Repository
public interface JobRepository extends JpaRepository<JobEntity, Long> {
}

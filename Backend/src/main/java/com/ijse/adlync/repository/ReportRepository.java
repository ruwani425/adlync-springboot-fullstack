package com.ijse.adlync.repository;

import com.ijse.adlync.entity.enums.ReportStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ijse.adlync.entity.ReportEntity;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {
    List<ReportEntity> findByStatus(ReportStatusEnum status);
    List<ReportEntity> findByStatusOrderByDateDesc(ReportStatusEnum status);
}

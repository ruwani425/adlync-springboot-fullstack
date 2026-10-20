package com.ijse.adlync.repository;

import com.ijse.adlync.entity.enums.ReportStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ijse.adlync.entity.ReportEntity;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {
    @Query("SELECT r FROM ReportEntity r LEFT JOIN FETCH r.post LEFT JOIN FETCH r.user WHERE r.status = :status ORDER BY r.date DESC")
    List<ReportEntity> findByStatusOrderByDateDesc(@Param("status") ReportStatusEnum status);

    @Query("SELECT r FROM ReportEntity r LEFT JOIN FETCH r.post LEFT JOIN FETCH r.user ORDER BY r.date DESC")
    List<ReportEntity> findAllWithDetails();

    List<ReportEntity> findByStatus(ReportStatusEnum status);
}

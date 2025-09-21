package com.ijse.adlync.service;

import com.ijse.adlync.dto.request.ReportRequestDTO;
import com.ijse.adlync.dto.response.ReportResponseDTO;

import java.util.List;

public interface ReportService {

    List<ReportResponseDTO> findAll();

    ReportResponseDTO findById(Long id);

    List<ReportResponseDTO> findByStatus(String status);

    ReportResponseDTO updateStatus(Long reportId, String status);

    ReportResponseDTO create(ReportRequestDTO requestDTO, String username);

    ReportResponseDTO create(ReportRequestDTO requestDTO);

    ReportResponseDTO update(Long id, ReportRequestDTO requestDTO);

    void deleteById(Long id);
}

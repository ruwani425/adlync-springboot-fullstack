package com.ijse.adlync.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.ijse.adlync.entity.ReportEntity;
import com.ijse.adlync.repository.ReportRepository;
import com.ijse.adlync.dto.request.ReportRequestDTO;
import com.ijse.adlync.dto.response.ReportResponseDTO;

@Service
public class ReportServiceImpl {

    @Autowired
    private ReportRepository repository;

    public List<ReportResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public ReportResponseDTO findById(Long id) {
        ReportEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("ReportEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public ReportResponseDTO create(ReportRequestDTO requestDTO) {
        ReportEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public ReportResponseDTO update(Long id, ReportRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("ReportEntity not found with id: " + id);
        }
        ReportEntity entity = toEntity(requestDTO);
        entity.setReport_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("ReportEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private ReportResponseDTO toResponseDTO(ReportEntity entity) {
        ReportResponseDTO dto = new ReportResponseDTO();
        dto.setReport_id(entity.getReport_id());
        dto.setReason(entity.getReason());
        dto.setDate(entity.getDate());
        return dto;
    }

    private ReportEntity toEntity(ReportRequestDTO dto) {
        ReportEntity entity = new ReportEntity();
        entity.setReason(dto.getReason());
        entity.setDate(dto.getDate());
        return entity;
    }
}

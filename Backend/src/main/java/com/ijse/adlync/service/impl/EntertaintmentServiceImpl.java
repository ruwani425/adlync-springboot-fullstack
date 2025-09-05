package com.ijse.adlync.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.ijse.adlync.entity.EntertaintmentEntity;
import com.ijse.adlync.repository.EntertaintmentRepository;
import com.ijse.adlync.dto.request.EntertaintmentRequestDTO;
import com.ijse.adlync.dto.response.EntertaintmentResponseDTO;

@Service
public class EntertaintmentServiceImpl {

    @Autowired
    private EntertaintmentRepository repository;

    public List<EntertaintmentResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public EntertaintmentResponseDTO findById(Long id) {
        EntertaintmentEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("EntertaintmentEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public EntertaintmentResponseDTO create(EntertaintmentRequestDTO requestDTO) {
        EntertaintmentEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public EntertaintmentResponseDTO update(Long id, EntertaintmentRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("EntertaintmentEntity not found with id: " + id);
        }
        EntertaintmentEntity entity = toEntity(requestDTO);
        entity.setId(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("EntertaintmentEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private EntertaintmentResponseDTO toResponseDTO(EntertaintmentEntity entity) {
        EntertaintmentResponseDTO dto = new EntertaintmentResponseDTO();
        dto.setId(entity.getId());
        dto.setType(entity.getType());
        dto.setFormat(entity.getFormat());
        dto.setBrand(entity.getBrand());
        return dto;
    }

    private EntertaintmentEntity toEntity(EntertaintmentRequestDTO dto) {
        EntertaintmentEntity entity = new EntertaintmentEntity();
        entity.setType(dto.getType());
        entity.setFormat(dto.getFormat());
        entity.setBrand(dto.getBrand());
        return entity;
    }
}

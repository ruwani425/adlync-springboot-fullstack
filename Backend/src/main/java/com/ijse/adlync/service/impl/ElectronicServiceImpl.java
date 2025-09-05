package com.ijse.adlync.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.ijse.adlync.entity.ElectronicEntity;
import com.ijse.adlync.repository.ElectronicRepository;
import com.ijse.adlync.dto.request.ElectronicRequestDTO;
import com.ijse.adlync.dto.response.ElectronicResponseDTO;

@Service
public class ElectronicServiceImpl {

    @Autowired
    private ElectronicRepository repository;

    public List<ElectronicResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public ElectronicResponseDTO findById(Long id) {
        ElectronicEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("ElectronicEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public ElectronicResponseDTO create(ElectronicRequestDTO requestDTO) {
        ElectronicEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public ElectronicResponseDTO update(Long id, ElectronicRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("ElectronicEntity not found with id: " + id);
        }
        ElectronicEntity entity = toEntity(requestDTO);
        entity.setElectronic_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("ElectronicEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private ElectronicResponseDTO toResponseDTO(ElectronicEntity entity) {
        ElectronicResponseDTO dto = new ElectronicResponseDTO();
        dto.setElectronic_id(entity.getElectronic_id());
        dto.setBrand(entity.getBrand());
        dto.setType(entity.getType());
        dto.setModel(entity.getModel());
        dto.setWarranty(entity.getWarranty());
        return dto;
    }

    private ElectronicEntity toEntity(ElectronicRequestDTO dto) {
        ElectronicEntity entity = new ElectronicEntity();
        entity.setBrand(dto.getBrand());
        entity.setType(dto.getType());
        entity.setModel(dto.getModel());
        entity.setWarranty(dto.getWarranty());
        return entity;
    }
}

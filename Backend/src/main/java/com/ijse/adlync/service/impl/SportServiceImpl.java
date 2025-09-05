package com.ijse.adlync.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.ijse.adlync.entity.SportEntity;
import com.ijse.adlync.repository.SportRepository;
import com.ijse.adlync.dto.request.SportRequestDTO;
import com.ijse.adlync.dto.response.SportResponseDTO;

@Service
public class SportServiceImpl {

    @Autowired
    private SportRepository repository;

    public List<SportResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public SportResponseDTO findById(Long id) {
        SportEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("SportEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public SportResponseDTO create(SportRequestDTO requestDTO) {
        SportEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public SportResponseDTO update(Long id, SportRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("SportEntity not found with id: " + id);
        }
        SportEntity entity = toEntity(requestDTO);
        entity.setSport_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("SportEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private SportResponseDTO toResponseDTO(SportEntity entity) {
        SportResponseDTO dto = new SportResponseDTO();
        dto.setSport_id(entity.getSport_id());
        dto.setEquipment_type(entity.getEquipment_type());
        dto.setBrand(entity.getBrand());
        dto.setCondition(entity.getCondition());
        dto.setSize(entity.getSize());
        return dto;
    }

    private SportEntity toEntity(SportRequestDTO dto) {
        SportEntity entity = new SportEntity();
        entity.setEquipment_type(dto.getEquipment_type());
        entity.setBrand(dto.getBrand());
        entity.setCondition(dto.getCondition());
        entity.setSize(dto.getSize());
        return entity;
    }
}

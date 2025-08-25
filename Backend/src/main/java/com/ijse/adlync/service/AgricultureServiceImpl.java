package com.ijse.adlync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.ijse.adlync.entity.AgricultureEntity;
import com.ijse.adlync.repository.AgricultureRepository;
import com.ijse.adlync.dto.request.AgricultureRequestDTO;
import com.ijse.adlync.dto.response.AgricultureResponseDTO;

@Service
public class AgricultureServiceImpl {

    @Autowired
    private AgricultureRepository repository;

    public List<AgricultureResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public AgricultureResponseDTO findById(Long id) {
        AgricultureEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("AgricultureEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public AgricultureResponseDTO create(AgricultureRequestDTO requestDTO) {
        AgricultureEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public AgricultureResponseDTO update(Long id, AgricultureRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("AgricultureEntity not found with id: " + id);
        }
        AgricultureEntity entity = toEntity(requestDTO);
        entity.setAgriculture_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("AgricultureEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private AgricultureResponseDTO toResponseDTO(AgricultureEntity entity) {
        AgricultureResponseDTO dto = new AgricultureResponseDTO();
        dto.setAgriculture_id(entity.getAgriculture_id());
        dto.setQuantity(entity.getQuantity());
        dto.setSeason(entity.getSeason());
        dto.setCondition(entity.getCondition());
        return dto;
    }

    private AgricultureEntity toEntity(AgricultureRequestDTO dto) {
        AgricultureEntity entity = new AgricultureEntity();
        entity.setQuantity(dto.getQuantity());
        entity.setSeason(dto.getSeason());
        entity.setCondition(dto.getCondition());
        return entity;
    }
}

package com.ijse.adlync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.ijse.adlync.entity.Work_over_seasEntity;
import com.ijse.adlync.repository.Work_over_seasRepository;
import com.ijse.adlync.dto.request.Work_over_seasRequestDTO;
import com.ijse.adlync.dto.response.Work_over_seasResponseDTO;

@Service
public class Work_over_seasServiceImpl {

    @Autowired
    private Work_over_seasRepository repository;

    public List<Work_over_seasResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public Work_over_seasResponseDTO findById(Long id) {
        Work_over_seasEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Work_over_seasEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public Work_over_seasResponseDTO create(Work_over_seasRequestDTO requestDTO) {
        Work_over_seasEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public Work_over_seasResponseDTO update(Long id, Work_over_seasRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Work_over_seasEntity not found with id: " + id);
        }
        Work_over_seasEntity entity = toEntity(requestDTO);
        entity.setOverseas_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Work_over_seasEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private Work_over_seasResponseDTO toResponseDTO(Work_over_seasEntity entity) {
        Work_over_seasResponseDTO dto = new Work_over_seasResponseDTO();
        dto.setOverseas_id(entity.getOverseas_id());
        dto.setPosition(entity.getPosition());
        dto.setCountry(entity.getCountry());
        dto.setSalary(entity.getSalary());
        dto.setRequirements(entity.getRequirements());
        dto.setContract_duration(entity.getContract_duration());
        return dto;
    }

    private Work_over_seasEntity toEntity(Work_over_seasRequestDTO dto) {
        Work_over_seasEntity entity = new Work_over_seasEntity();
        entity.setPosition(dto.getPosition());
        entity.setCountry(dto.getCountry());
        entity.setSalary(dto.getSalary());
        entity.setRequirements(dto.getRequirements());
        entity.setContract_duration(dto.getContract_duration());
        return entity;
    }
}

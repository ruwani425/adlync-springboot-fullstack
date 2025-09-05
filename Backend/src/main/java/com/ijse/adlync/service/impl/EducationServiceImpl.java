package com.ijse.adlync.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.ijse.adlync.entity.EducationEntity;
import com.ijse.adlync.repository.EducationRepository;
import com.ijse.adlync.dto.request.EducationRequestDTO;
import com.ijse.adlync.dto.response.EducationResponseDTO;

@Service
public class EducationServiceImpl {

    @Autowired
    private EducationRepository repository;

    public List<EducationResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public EducationResponseDTO findById(Long id) {
        EducationEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("EducationEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public EducationResponseDTO create(EducationRequestDTO requestDTO) {
        EducationEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public EducationResponseDTO update(Long id, EducationRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("EducationEntity not found with id: " + id);
        }
        EducationEntity entity = toEntity(requestDTO);
        entity.setEducation_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("EducationEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private EducationResponseDTO toResponseDTO(EducationEntity entity) {
        EducationResponseDTO dto = new EducationResponseDTO();
        dto.setEducation_id(entity.getEducation_id());
        dto.setCourse_name(entity.getCourse_name());
        dto.setInstitute(entity.getInstitute());
        dto.setDuration(entity.getDuration());
        dto.setQulification_offered(entity.getQulification_offered());
        return dto;
    }

    private EducationEntity toEntity(EducationRequestDTO dto) {
        EducationEntity entity = new EducationEntity();
        entity.setCourse_name(dto.getCourse_name());
        entity.setInstitute(dto.getInstitute());
        entity.setDuration(dto.getDuration());
        entity.setQulification_offered(dto.getQulification_offered());
        return entity;
    }
}

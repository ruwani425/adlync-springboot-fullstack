package com.ijse.adlync.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.ijse.adlync.entity.JobEntity;
import com.ijse.adlync.repository.JobRepository;
import com.ijse.adlync.dto.request.JobRequestDTO;
import com.ijse.adlync.dto.response.JobResponseDTO;

@Service
public class JobServiceImpl {

    @Autowired
    private JobRepository repository;

    public List<JobResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public JobResponseDTO findById(Long id) {
        JobEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("JobEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public JobResponseDTO create(JobRequestDTO requestDTO) {
        JobEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public JobResponseDTO update(Long id, JobRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("JobEntity not found with id: " + id);
        }
        JobEntity entity = toEntity(requestDTO);
        entity.setJob_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("JobEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private JobResponseDTO toResponseDTO(JobEntity entity) {
        JobResponseDTO dto = new JobResponseDTO();
        dto.setJob_id(entity.getJob_id());
        dto.setPosition(entity.getPosition());
        dto.setCompany(entity.getCompany());
        return dto;
    }

    private JobEntity toEntity(JobRequestDTO dto) {
        JobEntity entity = new JobEntity();
        entity.setPosition(dto.getPosition());
        entity.setCompany(dto.getCompany());
        return entity;
    }
}

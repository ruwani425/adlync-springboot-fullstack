package com.ijse.adlync.service.impl;

import com.ijse.adlync.entity.enums.Fashion_and_beautyEntityGenderEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.ijse.adlync.entity.Fashion_and_beautyEntity;
import com.ijse.adlync.repository.Fashion_and_beautyRepository;
import com.ijse.adlync.dto.request.Fashion_and_beautyRequestDTO;
import com.ijse.adlync.dto.response.Fashion_and_beautyResponseDTO;

@Service
public class Fashion_and_beautyServiceImpl {

    @Autowired
    private Fashion_and_beautyRepository repository;

    public List<Fashion_and_beautyResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public Fashion_and_beautyResponseDTO findById(Long id) {
        Fashion_and_beautyEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fashion_and_beautyEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public Fashion_and_beautyResponseDTO create(Fashion_and_beautyRequestDTO requestDTO) {
        Fashion_and_beautyEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public Fashion_and_beautyResponseDTO update(Long id, Fashion_and_beautyRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Fashion_and_beautyEntity not found with id: " + id);
        }
        Fashion_and_beautyEntity entity = toEntity(requestDTO);
        entity.setFashion_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Fashion_and_beautyEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private Fashion_and_beautyResponseDTO toResponseDTO(Fashion_and_beautyEntity entity) {
        Fashion_and_beautyResponseDTO dto = new Fashion_and_beautyResponseDTO();
        dto.setFashion_id(entity.getFashion_id());
        dto.setItem_type(entity.getItem_type());
        dto.setBrand(entity.getBrand());
        dto.setSize(entity.getSize());
        dto.setGender(String.valueOf(entity.getGender()));
        dto.setCondition(Fashion_and_beautyEntityGenderEnum.valueOf(entity.getCondition()));
        return dto;
    }

    private Fashion_and_beautyEntity toEntity(Fashion_and_beautyRequestDTO dto) {
        Fashion_and_beautyEntity entity = new Fashion_and_beautyEntity();
        entity.setItem_type(dto.getItem_type());
        entity.setBrand(dto.getBrand());
        entity.setSize(dto.getSize());
        entity.setGender(dto.getGender());
        entity.setCondition(dto.getCondition());
        return entity;
    }
}

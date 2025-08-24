package com.ijse.adlync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.ijse.adlync.entity.Advertisement_typeEntity;
import com.ijse.adlync.repository.Advertisement_typeRepository;
import com.ijse.adlync.dto.request.Advertisement_typeRequestDTO;
import com.ijse.adlync.dto.response.Advertisement_typeResponseDTO;

@Service
public class Advertisement_typeServiceImpl {

    @Autowired
    private Advertisement_typeRepository repository;

    public List<Advertisement_typeResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public Advertisement_typeResponseDTO findById(Long id) {
        Advertisement_typeEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Advertisement_typeEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public Advertisement_typeResponseDTO create(Advertisement_typeRequestDTO requestDTO) {
        Advertisement_typeEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public Advertisement_typeResponseDTO update(Long id, Advertisement_typeRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Advertisement_typeEntity not found with id: " + id);
        }
        Advertisement_typeEntity entity = toEntity(requestDTO);
        entity.setAd_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Advertisement_typeEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private Advertisement_typeResponseDTO toResponseDTO(Advertisement_typeEntity entity) {
        Advertisement_typeResponseDTO dto = new Advertisement_typeResponseDTO();
        dto.setAd_id(entity.getAd_id());
        dto.setType(entity.getType());
        return dto;
    }

    private Advertisement_typeEntity toEntity(Advertisement_typeRequestDTO dto) {
        Advertisement_typeEntity entity = new Advertisement_typeEntity();
        entity.setType(dto.getType());
        return entity;
    }
}

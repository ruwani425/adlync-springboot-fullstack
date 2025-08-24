package com.ijse.adlync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.ijse.adlync.entity.EssentialsEntity;
import com.ijse.adlync.repository.EssentialsRepository;
import com.ijse.adlync.dto.request.EssentialsRequestDTO;
import com.ijse.adlync.dto.response.EssentialsResponseDTO;

@Service
public class EssentialsServiceImpl {

    @Autowired
    private EssentialsRepository repository;

    public List<EssentialsResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public EssentialsResponseDTO findById(Long id) {
        EssentialsEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("EssentialsEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public EssentialsResponseDTO create(EssentialsRequestDTO requestDTO) {
        EssentialsEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public EssentialsResponseDTO update(Long id, EssentialsRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("EssentialsEntity not found with id: " + id);
        }
        EssentialsEntity entity = toEntity(requestDTO);
        entity.setEssential_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("EssentialsEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private EssentialsResponseDTO toResponseDTO(EssentialsEntity entity) {
        EssentialsResponseDTO dto = new EssentialsResponseDTO();
        dto.setEssential_id(entity.getEssential_id());
        dto.setItem_name(entity.getItem_name());
        dto.setBrand(entity.getBrand());
        dto.setQuantity(entity.getQuantity());
        dto.setUnit(entity.getUnit());
        dto.setExpiry_date(entity.getExpiry_date());
        return dto;
    }

    private EssentialsEntity toEntity(EssentialsRequestDTO dto) {
        EssentialsEntity entity = new EssentialsEntity();
        entity.setItem_name(dto.getItem_name());
        entity.setBrand(dto.getBrand());
        entity.setQuantity(dto.getQuantity());
        entity.setUnit(dto.getUnit());
        entity.setExpiry_date(dto.getExpiry_date());
        return entity;
    }
}

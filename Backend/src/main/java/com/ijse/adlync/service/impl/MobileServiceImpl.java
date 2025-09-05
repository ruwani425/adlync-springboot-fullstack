package com.ijse.adlync.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.ijse.adlync.entity.MobileEntity;
import com.ijse.adlync.repository.MobileRepository;
import com.ijse.adlync.dto.request.MobileRequestDTO;
import com.ijse.adlync.dto.response.MobileResponseDTO;

@Service
public class MobileServiceImpl {

    @Autowired
    private MobileRepository repository;

    public List<MobileResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public MobileResponseDTO findById(Long id) {
        MobileEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("MobileEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public MobileResponseDTO create(MobileRequestDTO requestDTO) {
        MobileEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public MobileResponseDTO update(Long id, MobileRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("MobileEntity not found with id: " + id);
        }
        MobileEntity entity = toEntity(requestDTO);
        entity.setMobile_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("MobileEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private MobileResponseDTO toResponseDTO(MobileEntity entity) {
        MobileResponseDTO dto = new MobileResponseDTO();
        dto.setMobile_id(entity.getMobile_id());
        dto.setStorage(entity.getStorage());
        dto.setCondition(entity.getCondition());
        dto.setRam(entity.getRam());
        dto.setBrand(entity.getBrand());
        return dto;
    }

    private MobileEntity toEntity(MobileRequestDTO dto) {
        MobileEntity entity = new MobileEntity();
        entity.setStorage(dto.getStorage());
        entity.setCondition(dto.getCondition());
        entity.setRam(dto.getRam());
        entity.setBrand(dto.getBrand());
        return entity;
    }
}

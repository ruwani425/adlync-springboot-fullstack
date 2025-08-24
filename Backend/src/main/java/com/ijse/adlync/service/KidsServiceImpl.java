package com.ijse.adlync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.ijse.adlync.entity.KidsEntity;
import com.ijse.adlync.repository.KidsRepository;
import com.ijse.adlync.dto.request.KidsRequestDTO;
import com.ijse.adlync.dto.response.KidsResponseDTO;

@Service
public class KidsServiceImpl {

    @Autowired
    private KidsRepository repository;

    public List<KidsResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public KidsResponseDTO findById(Long id) {
        KidsEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("KidsEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public KidsResponseDTO create(KidsRequestDTO requestDTO) {
        KidsEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public KidsResponseDTO update(Long id, KidsRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("KidsEntity not found with id: " + id);
        }
        KidsEntity entity = toEntity(requestDTO);
        entity.setKids_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("KidsEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private KidsResponseDTO toResponseDTO(KidsEntity entity) {
        KidsResponseDTO dto = new KidsResponseDTO();
        dto.setKids_id(entity.getKids_id());
        dto.setItem_type(entity.getItem_type());
        dto.setAge_range(entity.getAge_range());
        dto.setBrand(entity.getBrand());
        dto.setCondition(entity.getCondition());
        return dto;
    }

    private KidsEntity toEntity(KidsRequestDTO dto) {
        KidsEntity entity = new KidsEntity();
        entity.setItem_type(dto.getItem_type());
        entity.setAge_range(dto.getAge_range());
        entity.setBrand(dto.getBrand());
        entity.setCondition(dto.getCondition());
        return entity;
    }
}

package com.ijse.adlync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.ijse.adlync.entity.PropertyEntity;
import com.ijse.adlync.repository.PropertyRepository;
import com.ijse.adlync.dto.request.PropertyRequestDTO;
import com.ijse.adlync.dto.response.PropertyResponseDTO;

@Service
public class PropertyServiceImpl {

    @Autowired
    private PropertyRepository repository;

    public List<PropertyResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public PropertyResponseDTO findById(Long id) {
        PropertyEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("PropertyEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public PropertyResponseDTO create(PropertyRequestDTO requestDTO) {
        PropertyEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public PropertyResponseDTO update(Long id, PropertyRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("PropertyEntity not found with id: " + id);
        }
        PropertyEntity entity = toEntity(requestDTO);
        entity.setProperty_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("PropertyEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private PropertyResponseDTO toResponseDTO(PropertyEntity entity) {
        PropertyResponseDTO dto = new PropertyResponseDTO();
        dto.setProperty_id(entity.getProperty_id());
        dto.setType(entity.getType());
        dto.setLand_size(entity.getLand_size());
        dto.setBedroom(entity.getBedroom());
        dto.setBarthroom(entity.getBarthroom());
        dto.setFurnished(entity.getFurnished());
        dto.setLocation_details(entity.getLocation_details());
        return dto;
    }

    private PropertyEntity toEntity(PropertyRequestDTO dto) {
        PropertyEntity entity = new PropertyEntity();
        entity.setType(dto.getType());
        entity.setLand_size(dto.getLand_size());
        entity.setBedroom(dto.getBedroom());
        entity.setBarthroom(dto.getBarthroom());
        entity.setFurnished(dto.getFurnished());
        entity.setLocation_details(dto.getLocation_details());
        return entity;
    }
}

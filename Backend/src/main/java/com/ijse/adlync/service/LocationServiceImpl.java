package com.ijse.adlync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.ijse.adlync.entity.LocationEntity;
import com.ijse.adlync.repository.LocationRepository;
import com.ijse.adlync.dto.request.LocationRequestDTO;
import com.ijse.adlync.dto.response.LocationResponseDTO;

@Service
public class LocationServiceImpl {

    @Autowired
    private LocationRepository repository;

    public List<LocationResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public LocationResponseDTO findById(Long id) {
        LocationEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("LocationEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public LocationResponseDTO create(LocationRequestDTO requestDTO) {
        LocationEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public LocationResponseDTO update(Long id, LocationRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("LocationEntity not found with id: " + id);
        }
        LocationEntity entity = toEntity(requestDTO);
        entity.setLocation_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("LocationEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private LocationResponseDTO toResponseDTO(LocationEntity entity) {
        LocationResponseDTO dto = new LocationResponseDTO();
        dto.setLocation_id(entity.getLocation_id());
        dto.setCity(entity.getCity());
        dto.setDistrict(entity.getDistrict());
        return dto;
    }

    private LocationEntity toEntity(LocationRequestDTO dto) {
        LocationEntity entity = new LocationEntity();
        entity.setCity(dto.getCity());
        entity.setDistrict(dto.getDistrict());
        return entity;
    }
}

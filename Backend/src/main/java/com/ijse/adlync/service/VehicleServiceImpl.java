package com.ijse.adlync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.ijse.adlync.entity.VehicleEntity;
import com.ijse.adlync.repository.VehicleRepository;
import com.ijse.adlync.dto.request.VehicleRequestDTO;
import com.ijse.adlync.dto.response.VehicleResponseDTO;

@Service
public class VehicleServiceImpl {

    @Autowired
    private VehicleRepository repository;

    public List<VehicleResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public VehicleResponseDTO findById(Long id) {
        VehicleEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("VehicleEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public VehicleResponseDTO create(VehicleRequestDTO requestDTO) {
        VehicleEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public VehicleResponseDTO update(Long id, VehicleRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("VehicleEntity not found with id: " + id);
        }
        VehicleEntity entity = toEntity(requestDTO);
        entity.setVehicle_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("VehicleEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private VehicleResponseDTO toResponseDTO(VehicleEntity entity) {
        VehicleResponseDTO dto = new VehicleResponseDTO();
        dto.setVehicle_id(entity.getVehicle_id());
        dto.setMileage(entity.getMileage());
        dto.setYear(entity.getYear());
        dto.setBrand(entity.getBrand());
        dto.setModel(entity.getModel());
        dto.setFuel_type(entity.getFuel_type());
        dto.setTransmission(entity.getTransmission());
        dto.setCondition(entity.getCondition());
        return dto;
    }

    private VehicleEntity toEntity(VehicleRequestDTO dto) {
        VehicleEntity entity = new VehicleEntity();
        entity.setMileage(dto.getMileage());
        entity.setYear(dto.getYear());
        entity.setBrand(dto.getBrand());
        entity.setModel(dto.getModel());
        entity.setFuel_type(dto.getFuel_type());
        entity.setTransmission(dto.getTransmission());
        entity.setCondition(dto.getCondition());
        return entity;
    }
}

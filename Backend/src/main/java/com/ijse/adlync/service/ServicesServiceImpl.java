package com.ijse.adlync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.ijse.adlync.entity.ServicesEntity;
import com.ijse.adlync.repository.ServicesRepository;
import com.ijse.adlync.dto.request.ServicesRequestDTO;
import com.ijse.adlync.dto.response.ServicesResponseDTO;

@Service
public class ServicesServiceImpl {

    @Autowired
    private ServicesRepository repository;

    public List<ServicesResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public ServicesResponseDTO findById(Long id) {
        ServicesEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("ServicesEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public ServicesResponseDTO create(ServicesRequestDTO requestDTO) {
        ServicesEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public ServicesResponseDTO update(Long id, ServicesRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("ServicesEntity not found with id: " + id);
        }
        ServicesEntity entity = toEntity(requestDTO);
        entity.setService_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("ServicesEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private ServicesResponseDTO toResponseDTO(ServicesEntity entity) {
        ServicesResponseDTO dto = new ServicesResponseDTO();
        dto.setService_id(entity.getService_id());
        dto.setService_type(entity.getService_type());
        dto.setProvider_name(entity.getProvider_name());
        dto.setAvailability(entity.getAvailability());
        dto.setCharges(entity.getCharges());
        return dto;
    }

    private ServicesEntity toEntity(ServicesRequestDTO dto) {
        ServicesEntity entity = new ServicesEntity();
        entity.setService_type(dto.getService_type());
        entity.setProvider_name(dto.getProvider_name());
        entity.setAvailability(dto.getAvailability());
        entity.setCharges(dto.getCharges());
        return entity;
    }
}

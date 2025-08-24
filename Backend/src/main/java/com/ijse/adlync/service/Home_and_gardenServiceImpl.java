package com.ijse.adlync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.ijse.adlync.entity.Home_and_gardenEntity;
import com.ijse.adlync.repository.Home_and_gardenRepository;
import com.ijse.adlync.dto.request.Home_and_gardenRequestDTO;
import com.ijse.adlync.dto.response.Home_and_gardenResponseDTO;

@Service
public class Home_and_gardenServiceImpl {

    @Autowired
    private Home_and_gardenRepository repository;

    public List<Home_and_gardenResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public Home_and_gardenResponseDTO findById(Long id) {
        Home_and_gardenEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Home_and_gardenEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public Home_and_gardenResponseDTO create(Home_and_gardenRequestDTO requestDTO) {
        Home_and_gardenEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public Home_and_gardenResponseDTO update(Long id, Home_and_gardenRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Home_and_gardenEntity not found with id: " + id);
        }
        Home_and_gardenEntity entity = toEntity(requestDTO);
        entity.setHome_garden_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Home_and_gardenEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private Home_and_gardenResponseDTO toResponseDTO(Home_and_gardenEntity entity) {
        Home_and_gardenResponseDTO dto = new Home_and_gardenResponseDTO();
        dto.setHome_garden_id(entity.getHome_garden_id());
        dto.setItem_type(entity.getItem_type());
        dto.setMaterial(entity.getMaterial());
        dto.setDimensions(entity.getDimensions());
        dto.setCondition(entity.getCondition());
        return dto;
    }

    private Home_and_gardenEntity toEntity(Home_and_gardenRequestDTO dto) {
        Home_and_gardenEntity entity = new Home_and_gardenEntity();
        entity.setItem_type(dto.getItem_type());
        entity.setMaterial(dto.getMaterial());
        entity.setDimensions(dto.getDimensions());
        entity.setCondition(dto.getCondition());
        return entity;
    }
}

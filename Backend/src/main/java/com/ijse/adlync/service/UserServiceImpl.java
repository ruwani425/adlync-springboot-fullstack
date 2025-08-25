package com.ijse.adlync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.ijse.adlync.entity.UserEntity;
import com.ijse.adlync.repository.UserRepository;
import com.ijse.adlync.dto.request.RegisterRequestDTO;
import com.ijse.adlync.dto.response.RegisterResponseDTO;

@Service
public class UserServiceImpl {

    @Autowired
    private UserRepository repository;

    public List<RegisterResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public RegisterResponseDTO findById(Long id) {
        UserEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("UserEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public RegisterResponseDTO create(RegisterRequestDTO requestDTO) {
        UserEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public RegisterResponseDTO update(Long id, RegisterRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("UserEntity not found with id: " + id);
        }
        UserEntity entity = toEntity(requestDTO);
        entity.setId(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("UserEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private RegisterResponseDTO toResponseDTO(UserEntity entity) {
        RegisterResponseDTO dto = new RegisterResponseDTO();
        dto.setId(entity.getId());
        dto.setPassword(entity.getPassword());
        dto.setRole(entity.getRole());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        return dto;
    }

    private UserEntity toEntity(RegisterRequestDTO dto) {
        UserEntity entity = new UserEntity();
        entity.setPassword(dto.getPassword());
        entity.setRole(dto.getRole());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        return entity;
    }
}

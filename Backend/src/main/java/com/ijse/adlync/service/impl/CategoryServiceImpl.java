package com.ijse.adlync.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.ijse.adlync.entity.CategoryEntity;
import com.ijse.adlync.repository.CategoryRepository;
import com.ijse.adlync.dto.request.CategoryRequestDTO;
import com.ijse.adlync.dto.response.CategoryResponseDTO;

@Service
public class CategoryServiceImpl {

    @Autowired
    private CategoryRepository repository;

    public List<CategoryResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public CategoryResponseDTO findById(Long id) {
        CategoryEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("CategoryEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public CategoryResponseDTO create(CategoryRequestDTO requestDTO) {
        CategoryEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public CategoryResponseDTO update(Long id, CategoryRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("CategoryEntity not found with id: " + id);
        }
        CategoryEntity entity = toEntity(requestDTO);
        entity.setCategory_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("CategoryEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private CategoryResponseDTO toResponseDTO(CategoryEntity entity) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setCategory_id(entity.getCategory_id());
        dto.setName(entity.getName());
        return dto;
    }

    private CategoryEntity toEntity(CategoryRequestDTO dto) {
        CategoryEntity entity = new CategoryEntity();
        entity.setName(dto.getName());
        return entity;
    }
}

package com.ijse.adlync.service.impl;

import com.ijse.adlync.dto.request.CategoryRequestDTO;
import com.ijse.adlync.dto.response.CategoryResponseDTO;
import com.ijse.adlync.entity.CategoryEntity;
import com.ijse.adlync.repository.CategoryRepository;
import com.ijse.adlync.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Override
    public List<CategoryResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDTO findById(Long id) {
        CategoryEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("CategoryEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    @Override
    public CategoryResponseDTO create(CategoryRequestDTO requestDTO) {
        CategoryEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    @Override
    public CategoryResponseDTO update(Long id, CategoryRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("CategoryEntity not found with id: " + id);
        }
        CategoryEntity entity = toEntity(requestDTO);
        entity.setCategory_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("CategoryEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private CategoryResponseDTO toResponseDTO(CategoryEntity entity) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setName(entity.getName());
        return dto;
    }

    private CategoryEntity toEntity(CategoryRequestDTO dto) {
        CategoryEntity entity = new CategoryEntity();
        entity.setName(dto.getName());
        return entity;
    }
}

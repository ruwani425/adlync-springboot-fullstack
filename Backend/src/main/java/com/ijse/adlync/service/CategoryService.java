package com.ijse.adlync.service;

import com.ijse.adlync.dto.request.CategoryRequestDTO;
import com.ijse.adlync.dto.response.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {

    List<CategoryResponseDTO> findAll();

    CategoryResponseDTO findById(Long id);

    CategoryResponseDTO create(CategoryRequestDTO requestDTO);

    CategoryResponseDTO update(Long id, CategoryRequestDTO requestDTO);

    void deleteById(Long id);
}

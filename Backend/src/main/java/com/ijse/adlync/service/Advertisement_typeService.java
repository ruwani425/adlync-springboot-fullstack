package com.ijse.adlync.service;

import com.ijse.adlync.dto.request.Advertisement_typeRequestDTO;
import com.ijse.adlync.dto.response.Advertisement_typeResponseDTO;

import java.util.List;


public interface Advertisement_typeService {
    List<Advertisement_typeResponseDTO> findAll();

    Advertisement_typeResponseDTO findById(Long id);

    Advertisement_typeResponseDTO create(Advertisement_typeRequestDTO requestDTO);

    Advertisement_typeResponseDTO update(Long id, Advertisement_typeRequestDTO requestDTO);

    void deleteById(Long id);
}

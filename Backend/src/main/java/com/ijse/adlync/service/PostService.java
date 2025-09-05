package com.ijse.adlync.service;

import com.ijse.adlync.dto.request.AnimalRequestDTO;
import com.ijse.adlync.dto.request.PostRequestDTO;
import com.ijse.adlync.dto.request.PropertyRequestDTO;
import com.ijse.adlync.dto.request.VehicleRequestDTO;
import com.ijse.adlync.dto.response.PostResponseDTO;

import java.util.List;

public interface PostService {
    List<PostResponseDTO> findAll();

    PostResponseDTO findById(Long id);

    PostResponseDTO create(PostRequestDTO requestDTO);

    PostResponseDTO update(Long id, PostRequestDTO requestDTO);

    void deleteById(Long id);

    String createAnimalPost(AnimalRequestDTO requestDTO, String username);

    String createVehiclePost(VehicleRequestDTO requestDTO, String username);

    String createPropertyPost(PropertyRequestDTO requestDTO, String username);
}

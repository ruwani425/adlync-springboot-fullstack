package com.ijse.adlync.service;

import com.ijse.adlync.dto.request.LocationRequestDTO;
import com.ijse.adlync.dto.response.LocationResponseDTO;

import java.util.List;

public interface LocationService {

    List<LocationResponseDTO> findAll();

    LocationResponseDTO findById(Long id);

    LocationResponseDTO create(LocationRequestDTO requestDTO);

    LocationResponseDTO update(Long id, LocationRequestDTO requestDTO);

    void deleteById(Long id);
}

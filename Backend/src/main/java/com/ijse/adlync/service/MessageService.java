package com.ijse.adlync.service;

import com.ijse.adlync.dto.request.MessageRequestDTO;
import com.ijse.adlync.dto.response.MessageResponseDTO;

import java.util.List;

public interface MessageService {
    List<MessageResponseDTO> findAll();

    MessageResponseDTO findById(Long id);

    MessageResponseDTO create(MessageRequestDTO requestDTO);

    MessageResponseDTO update(Long id, MessageRequestDTO requestDTO);

    void deleteById(Long id);

    MessageResponseDTO sendMessage(MessageRequestDTO requestDTO);
}

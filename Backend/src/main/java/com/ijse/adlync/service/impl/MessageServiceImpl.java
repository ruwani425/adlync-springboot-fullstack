package com.ijse.adlync.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.ijse.adlync.entity.MessageEntity;
import com.ijse.adlync.repository.MessageRepository;
import com.ijse.adlync.dto.request.MessageRequestDTO;
import com.ijse.adlync.dto.response.MessageResponseDTO;

@Service
public class MessageServiceImpl {

    @Autowired
    private MessageRepository repository;

    public List<MessageResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public MessageResponseDTO findById(Long id) {
        MessageEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("MessageEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public MessageResponseDTO create(MessageRequestDTO requestDTO) {
        MessageEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public MessageResponseDTO update(Long id, MessageRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("MessageEntity not found with id: " + id);
        }
        MessageEntity entity = toEntity(requestDTO);
        entity.setMessage_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("MessageEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private MessageResponseDTO toResponseDTO(MessageEntity entity) {
        MessageResponseDTO dto = new MessageResponseDTO();
        dto.setMessage_id(entity.getMessage_id());
        dto.setSent_at(entity.getSent_at());
        dto.setContent(entity.getContent());
        return dto;
    }

    private MessageEntity toEntity(MessageRequestDTO dto) {
        MessageEntity entity = new MessageEntity();
        entity.setSent_at(dto.getSent_at());
        entity.setContent(dto.getContent());
        return entity;
    }
}

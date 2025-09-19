package com.ijse.adlync.service.impl;

import com.ijse.adlync.dto.request.MessageRequestDTO;
import com.ijse.adlync.dto.response.MessageResponseDTO;
import com.ijse.adlync.entity.ChatEntity;
import com.ijse.adlync.entity.MessageEntity;
import com.ijse.adlync.entity.UserEntity;
import com.ijse.adlync.repository.MessageRepository;
import com.ijse.adlync.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository repository;

    @Override
    public List<MessageResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MessageResponseDTO findById(Long id) {
        MessageEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("MessageEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    @Override
    public MessageResponseDTO create(MessageRequestDTO requestDTO) {
        MessageEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    @Override
    public MessageResponseDTO update(Long id, MessageRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("MessageEntity not found with id: " + id);
        }
        MessageEntity entity = toEntity(requestDTO);
        entity.setMessage_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("MessageEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public MessageResponseDTO sendMessage(MessageRequestDTO dto) {
        MessageEntity entity = new MessageEntity();
        UserEntity userEntity = new UserEntity();
        ChatEntity chatEntity = new ChatEntity();
        userEntity.setId(dto.getSenderUserId());
        chatEntity.setChat_id(dto.getChatId());
        entity.setContent(dto.getContent());
        entity.setSent_at(LocalDateTime.now());
        entity.setSenderUser(userEntity);
        entity.setChat(chatEntity);
        entity = repository.save(entity);

        return toResponseDTO(entity);
    }

    private MessageResponseDTO toResponseDTO(MessageEntity entity) {
        MessageResponseDTO dto = new MessageResponseDTO();
        dto.setMessage_id(entity.getMessage_id());
        dto.setSent_at(entity.getSent_at());
        dto.setContent(entity.getContent());
        dto.setSenderUserId(entity.getSenderUser() != null ? entity.getSenderUser().getId() : null);
        dto.setSenderUserName(entity.getSenderUser() != null ?
                entity.getSenderUser().getName() : null);
        dto.setChatId(entity.getChat() != null ? entity.getChat().getChat_id() : null);
        return dto;
    }

    private MessageEntity toEntity(MessageRequestDTO dto) {
        MessageEntity entity = new MessageEntity();
        entity.setContent(dto.getContent());
        entity.setSent_at(LocalDateTime.now());
        if (dto.getSenderUserId() != null) {
            UserEntity user = new UserEntity();
            user.setId(dto.getSenderUserId());
            entity.setSenderUser(user);
        }
        if (dto.getChatId() != null) {
            ChatEntity chat = new ChatEntity();
            chat.setChat_id(dto.getChatId());
            entity.setChat(chat);
        }
        return entity;
    }
}

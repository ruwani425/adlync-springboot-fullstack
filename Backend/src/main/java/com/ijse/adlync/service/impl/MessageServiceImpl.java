package com.ijse.adlync.service.impl;

import com.ijse.adlync.dto.request.MessageRequestDTO;
import com.ijse.adlync.dto.response.MessageResponseDTO;
import com.ijse.adlync.entity.MessageEntity;
import com.ijse.adlync.entity.PostEntity;
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
        PostEntity postEntity = new PostEntity();
        userEntity.setId(dto.getFromUserId());
        postEntity.setPost_id(dto.getPostId());
        entity.setContent(dto.getContent());
        entity.setSent_at(LocalDateTime.now());
        entity.setUser(userEntity);
        entity.setPost(postEntity);
        entity = repository.save(entity);

        return toResponseDTO(entity);
    }

    private MessageResponseDTO toResponseDTO(MessageEntity entity) {
        MessageResponseDTO dto = new MessageResponseDTO();
        dto.setMessage_id(entity.getMessage_id());
        dto.setSent_at(entity.getSent_at());
        dto.setContent(entity.getContent());
        dto.setToUser_id(entity.getUser() != null ? entity.getUser().getId() : null);
        dto.setPostId(entity.getPost() != null ? entity.getPost().getPost_id() : null);
        return dto;
    }

    private MessageEntity toEntity(MessageRequestDTO dto) {
        MessageEntity entity = new MessageEntity();
        entity.setContent(dto.getContent());
        entity.setSent_at(LocalDateTime.now());
        if (dto.getFromUserId() != null) {
            UserEntity user = new UserEntity();
            user.setId(dto.getFromUserId());
            entity.setUser(user);
        }
        if (dto.getPostId() != null) {
            PostEntity post = new PostEntity();
            post.setPost_id(dto.getPostId());
            entity.setPost(post);
        }
        return entity;
    }
}

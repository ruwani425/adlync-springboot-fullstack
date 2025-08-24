package com.ijse.adlync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.ijse.adlync.entity.PostEntity;
import com.ijse.adlync.repository.PostRepository;
import com.ijse.adlync.dto.request.PostRequestDTO;
import com.ijse.adlync.dto.response.PostResponseDTO;

@Service
public class PostServiceImpl {

    @Autowired
    private PostRepository repository;

    public List<PostResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public PostResponseDTO findById(Long id) {
        PostEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("PostEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public PostResponseDTO create(PostRequestDTO requestDTO) {
        PostEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public PostResponseDTO update(Long id, PostRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("PostEntity not found with id: " + id);
        }
        PostEntity entity = toEntity(requestDTO);
        entity.setPost_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("PostEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private PostResponseDTO toResponseDTO(PostEntity entity) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setPost_id(entity.getPost_id());
        dto.setStatus(entity.getStatus());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    private PostEntity toEntity(PostRequestDTO dto) {
        PostEntity entity = new PostEntity();
        entity.setStatus(dto.getStatus());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        return entity;
    }
}

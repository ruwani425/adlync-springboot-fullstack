package com.ijse.adlync.service.impl;

import com.ijse.adlync.dto.request.AgricultureRequestDTO;
import com.ijse.adlync.dto.response.AgricultureResponseDTO;
import com.ijse.adlync.dto.response.PostResponseDTO;
import com.ijse.adlync.entity.AgricultureEntity;
import com.ijse.adlync.entity.PostEntity;
import com.ijse.adlync.repository.AgricultureRepository;
import com.ijse.adlync.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgricultureServiceImpl {

    private final AgricultureRepository repository;
    private final PostRepository postRepository;

    public List<AgricultureResponseDTO> findAll() {
        return repository.findAll().stream().map(this::toResponseDTO).toList();
    }

    public AgricultureResponseDTO findById(Long id) {
        return repository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("AgricultureEntity not found with id: " + id));
    }

    @Transactional
    public AgricultureResponseDTO create(AgricultureRequestDTO requestDTO) {
        if (requestDTO.getPostRequestDTO() == null) {
            throw new IllegalArgumentException("Post data is required");
        }

        PostEntity savedPost = postRepository.save(createPostEntity(requestDTO));
        AgricultureEntity savedEntity = repository.save(createAgricultureEntity(requestDTO, savedPost));

        return toResponseDTO(savedEntity);
    }

    @Transactional
    public AgricultureResponseDTO update(Long id, AgricultureRequestDTO requestDTO) {
        AgricultureEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("AgricultureEntity not found with id: " + id));

        updateAgricultureFields(entity, requestDTO);
        updatePostFields(entity.getPost(), requestDTO);

        return toResponseDTO(repository.save(entity));
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("AgricultureEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private PostEntity createPostEntity(AgricultureRequestDTO requestDTO) {
        PostEntity post = new PostEntity();
        post.setStatus(requestDTO.getPostRequestDTO().getStatus());
        post.setTitle(requestDTO.getPostRequestDTO().getTitle());
        post.setDescription(requestDTO.getPostRequestDTO().getDescription());
        post.setContact_number(requestDTO.getPostRequestDTO().getContact_number());
        post.setPrice(requestDTO.getPostRequestDTO().getPrice());
        return post;
    }

    private AgricultureEntity createAgricultureEntity(AgricultureRequestDTO requestDTO, PostEntity post) {
        AgricultureEntity entity = new AgricultureEntity();
        entity.setProduct_type(requestDTO.getProduct_type());
        entity.setQuantity(requestDTO.getQuantity());
        entity.setSeason(requestDTO.getSeason());
        entity.setVariety(requestDTO.getVariety());
        entity.setProduction_Date(requestDTO.getProduction_Date());
        entity.setCertifications(requestDTO.getCertifications());
        entity.setCondition(requestDTO.getCondition());
        entity.setPost(post);
        return entity;
    }

    private void updateAgricultureFields(AgricultureEntity entity, AgricultureRequestDTO requestDTO) {
        entity.setProduct_type(requestDTO.getProduct_type());
        entity.setQuantity(requestDTO.getQuantity());
        entity.setSeason(requestDTO.getSeason());
        entity.setVariety(requestDTO.getVariety());
        entity.setProduction_Date(requestDTO.getProduction_Date());
        entity.setCertifications(requestDTO.getCertifications());
        entity.setCondition(requestDTO.getCondition());
    }

    private void updatePostFields(PostEntity post, AgricultureRequestDTO requestDTO) {
        if (requestDTO.getPostRequestDTO() != null && post != null) {
            post.setStatus(requestDTO.getPostRequestDTO().getStatus());
            post.setTitle(requestDTO.getPostRequestDTO().getTitle());
            post.setDescription(requestDTO.getPostRequestDTO().getDescription());
            post.setContact_number(requestDTO.getPostRequestDTO().getContact_number());
            post.setPrice(requestDTO.getPostRequestDTO().getPrice());
            postRepository.save(post);
        }
    }

    private AgricultureResponseDTO toResponseDTO(AgricultureEntity entity) {
        AgricultureResponseDTO dto = new AgricultureResponseDTO();
        dto.setAgriculture_id(entity.getAgriculture_id());
        dto.setProduct_type(entity.getProduct_type());
        dto.setQuantity(entity.getQuantity());
        dto.setSeason(entity.getSeason());
        dto.setVariety(entity.getVariety());
        dto.setProduction_Date(entity.getProduction_Date());
        dto.setCertifications(entity.getCertifications());
        dto.setCondition(entity.getCondition());

        if (entity.getPost() != null) {
            dto.setPostResponseDTO(toPostResponseDTO(entity.getPost()));
        }
        return dto;
    }

    private PostResponseDTO toPostResponseDTO(PostEntity entity) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setPost_id(entity.getPost_id());
        dto.setStatus(entity.getStatus());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setContact_number(entity.getContact_number());
        dto.setPrice(entity.getPrice());
        return dto;
    }
}
package com.ijse.adlync.service.impl;

import com.ijse.adlync.dto.request.AnimalRequestDTO;
import com.ijse.adlync.dto.response.AnimalResponseDTO;
import com.ijse.adlync.entity.AnimalEntity;
import com.ijse.adlync.entity.PostEntity;
import com.ijse.adlync.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimalServiceImpl {

    @Autowired
    private AnimalRepository repository;

    public List<AnimalResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public AnimalResponseDTO findById(Long id) {
        AnimalEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("AnimalEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    @Transactional
    public AnimalResponseDTO create(AnimalRequestDTO requestDTO) {
        AnimalEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public AnimalResponseDTO update(Long id, AnimalRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("AnimalEntity not found with id: " + id);
        }
        AnimalEntity entity = toEntity(requestDTO);
        entity.setAnimal_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("AnimalEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private AnimalResponseDTO toResponseDTO(AnimalEntity entity) {
        AnimalResponseDTO dto = new AnimalResponseDTO();
        dto.setAnimal_id(entity.getAnimal_id());
        dto.setSpecies(entity.getSpecies());
        dto.setBreed(entity.getBreed());
        dto.setAge(entity.getAge());
        dto.setGender(entity.getGender());
        dto.setVaccination_status(entity.getVaccination_status());
        return dto;
    }

    private AnimalEntity toEntity(AnimalRequestDTO dto) {
        AnimalEntity entity = new AnimalEntity();
        PostEntity post = new PostEntity();
//        CategoryEntity category = new CategoryEntity();
//        category.setName(CategoryEntityNameEnum.ANIMAL);
//        post.setPost_id(dto.getPostRequestDTO().getPost_id());

        entity.setSpecies(dto.getSpecies());
        entity.setBreed(dto.getBreed());
        entity.setAge(dto.getAge());
        entity.setGender(dto.getGender());
        entity.setVaccination_status(dto.getVaccination_status());
        entity.setPost(post);
        return entity;
    }
}

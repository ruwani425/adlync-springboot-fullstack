package com.ijse.adlync.controller;

import com.ijse.adlync.dto.response.CategoryResponseDTO;
import com.ijse.adlync.dto.response.PostResponseDTO;
import com.ijse.adlync.dto.response.UserResponseDTO;
import com.ijse.adlync.entity.PostEntity;
import com.ijse.adlync.repository.CategoryRepository;
import com.ijse.adlync.repository.PostRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Tag(name = "test", description = "test")
public class TestController {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get all posts", description = "Retrieve a list of all posts")
    public PostResponseDTO getPostById(@PathVariable Long id) {
        Optional<PostEntity> post = postRepository.findById(id);
        System.out.println(post.get().getUser().getId());
        PostResponseDTO responseDTO = modelMapper.map(post, PostResponseDTO.class);
        responseDTO.setUser(modelMapper.map(post.get().getUser(), UserResponseDTO.class));
        responseDTO.setCategory(modelMapper.map(categoryRepository.findById(post.get().getCategory().getCategory_id()), CategoryResponseDTO.class));
        return responseDTO;
    }
}

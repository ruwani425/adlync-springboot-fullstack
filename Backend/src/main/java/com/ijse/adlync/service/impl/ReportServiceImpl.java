package com.ijse.adlync.service.impl;

import com.ijse.adlync.dto.request.ReportRequestDTO;
import com.ijse.adlync.dto.response.ReportResponseDTO;
import com.ijse.adlync.entity.PostEntity;
import com.ijse.adlync.entity.ReportEntity;
import com.ijse.adlync.entity.UserEntity;
import com.ijse.adlync.entity.enums.ReportStatusEnum;
import com.ijse.adlync.repository.PostRepository;
import com.ijse.adlync.repository.ReportRepository;
import com.ijse.adlync.repository.UserRepository;
import com.ijse.adlync.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    public List<ReportResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ReportResponseDTO findById(Long id) {
        ReportEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found with id: " + id));
        return toResponseDTO(entity);
    }

    public List<ReportResponseDTO> findByStatus(String status) {
        try {
            ReportStatusEnum statusEnum = ReportStatusEnum.valueOf(status);
            return repository.findByStatusOrderByDateDesc(statusEnum).stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }


    @Transactional
    public ReportResponseDTO updateStatus(Long reportId, String status) {
        ReportEntity report = repository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found with id: " + reportId));

        try {
            ReportStatusEnum statusEnum = ReportStatusEnum.valueOf(status);
            report.setStatus(statusEnum);
            repository.save(report);
            return toResponseDTO(report);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    @Transactional
    public ReportResponseDTO create(ReportRequestDTO requestDTO, String username) {
        if (requestDTO.getReason() == null || requestDTO.getReason().trim().isEmpty()) {
            throw new IllegalArgumentException("Report reason is required");
        }

        if (requestDTO.getPostId() == null) {
            throw new IllegalArgumentException("Post ID is required");
        }

        PostEntity post = postRepository.findById(requestDTO.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + requestDTO.getPostId()));

        ReportEntity entity = new ReportEntity();
        entity.setReason(requestDTO.getReason());
        entity.setCustomReason(requestDTO.getCustomReason());
        entity.setDescription(requestDTO.getDescription());
        entity.setReporterContact(requestDTO.getReporterContact());
        entity.setAnonymous(requestDTO.getAnonymous() != null ? requestDTO.getAnonymous() : true);
        entity.setDate(LocalDateTime.now());
        entity.setStatus(requestDTO.getStatus());
        entity.setPost(post);

        if (username != null && !username.trim().isEmpty() &&
                (requestDTO.getAnonymous() == null || !requestDTO.getAnonymous())) {
            UserEntity user = userRepository.findByUsername(username).orElse(null);
            entity.setUser(user);
        }

        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public ReportResponseDTO create(ReportRequestDTO requestDTO) {
        return create(requestDTO, null);
    }

    public ReportResponseDTO update(Long id, ReportRequestDTO requestDTO) {
        ReportEntity existingEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found with id: " + id));

        existingEntity.setReason(requestDTO.getReason());
        existingEntity.setCustomReason(requestDTO.getCustomReason());
        existingEntity.setDescription(requestDTO.getDescription());
        existingEntity.setReporterContact(requestDTO.getReporterContact());
        existingEntity.setAnonymous(requestDTO.getAnonymous());

        if (requestDTO.getPostId() != null && !requestDTO.getPostId().equals(existingEntity.getPost().getPost_id())) {
            PostEntity post = postRepository.findById(requestDTO.getPostId())
                    .orElseThrow(() -> new RuntimeException("Post not found with id: " + requestDTO.getPostId()));
            existingEntity.setPost(post);
        }

        existingEntity = repository.save(existingEntity);
        return toResponseDTO(existingEntity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Report not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private ReportResponseDTO toResponseDTO(ReportEntity entity) {
        ReportResponseDTO dto = new ReportResponseDTO();
        dto.setReport_id(entity.getReport_id());
        dto.setReason(entity.getReason());
        dto.setCustomReason(entity.getCustomReason());
        dto.setDescription(entity.getDescription());
        dto.setReporterContact(entity.getReporterContact());
        dto.setAnonymous(entity.getAnonymous());
        dto.setDate(entity.getDate());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus() : ReportStatusEnum.PENDING);

        if (entity.getPost() != null) {
            dto.setPostId(entity.getPost().getPost_id());
            dto.setPostTitle(entity.getPost().getTitle());
        }

        if (entity.getUser() != null && (entity.getAnonymous() == null || !entity.getAnonymous())) {
            dto.setReporterName(entity.getUser().getName());
        }

        return dto;
    }
}
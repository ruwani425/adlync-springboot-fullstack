package com.ijse.adlync.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.ijse.adlync.entity.PaymentEntity;
import com.ijse.adlync.repository.PaymentRepository;
import com.ijse.adlync.dto.request.PaymentRequestDTO;
import com.ijse.adlync.dto.response.PaymentResponseDTO;

@Service
public class PaymentServiceImpl {

    @Autowired
    private PaymentRepository repository;

    public List<PaymentResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public PaymentResponseDTO findById(Long id) {
        PaymentEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("PaymentEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public PaymentResponseDTO create(PaymentRequestDTO requestDTO) {
        PaymentEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public PaymentResponseDTO update(Long id, PaymentRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("PaymentEntity not found with id: " + id);
        }
        PaymentEntity entity = toEntity(requestDTO);
        entity.setPayment_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("PaymentEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private PaymentResponseDTO toResponseDTO(PaymentEntity entity) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setPayment_id(entity.getPayment_id());
        dto.setPayment_date(entity.getPayment_date());
        dto.setStatus(entity.getStatus());
        dto.setPayment_type(entity.getPayment_type());
        dto.setAmount(entity.getAmount());
        return dto;
    }

    private PaymentEntity toEntity(PaymentRequestDTO dto) {
        PaymentEntity entity = new PaymentEntity();
        entity.setPayment_date(dto.getPayment_date());
        entity.setStatus(dto.getStatus());
        entity.setPayment_type(dto.getPayment_type());
        entity.setAmount(dto.getAmount());
        return entity;
    }
}

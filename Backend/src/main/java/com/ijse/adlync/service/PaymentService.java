package com.ijse.adlync.service;

import com.ijse.adlync.dto.request.PaymentRequestDTO;
import com.ijse.adlync.dto.response.PaymentResponseDTO;

import java.util.List;

public interface PaymentService {

    List<PaymentResponseDTO> findAll();

    PaymentResponseDTO findById(Long id);

    PaymentResponseDTO create(PaymentRequestDTO requestDTO);

    PaymentResponseDTO update(Long id, PaymentRequestDTO requestDTO);

    void deleteById(Long id);
}

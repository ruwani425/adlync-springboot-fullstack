package com.ijse.adlync.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponseDTO {
    private int code;
    private String status;
    private Object data;
}

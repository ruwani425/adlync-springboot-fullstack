package com.ijse.adlync.dto.response;

import com.ijse.adlync.entity.enums.UserEntityRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginResponseDTO {
    private String token;
    private UserEntityRoleEnum role;
}

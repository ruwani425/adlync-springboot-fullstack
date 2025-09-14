package com.ijse.adlync.dto.response;

import com.ijse.adlync.entity.enums.UserEntityRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private UserEntityRoleEnum role;
    private String status;
    private LocalDateTime joinDate;
}

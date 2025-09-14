package com.ijse.adlync.dto.request;

import com.ijse.adlync.entity.enums.UserEntityRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserRequestDTO {
    private Long id;
    private UserEntityRoleEnum role;
    private String name;
    private String email;
    private String status;
    private LocalDateTime joinDate;
}

package com.ijse.adlync.dto.request;

import com.ijse.adlync.entity.enums.UserEntityRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {

    private String password;
    private UserEntityRoleEnum role;
    private String name;
    private String email;
}

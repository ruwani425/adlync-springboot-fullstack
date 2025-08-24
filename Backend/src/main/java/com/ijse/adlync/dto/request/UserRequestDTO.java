package com.ijse.adlync.dto.request;

import com.ijse.adlync.entity.enums.UserEntityRoleEnum;

public class UserRequestDTO {

    private String password;
    private UserEntityRoleEnum role;
    private String name;
    private String email;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserEntityRoleEnum getRole() {
        return role;
    }

    public void setRole(UserEntityRoleEnum role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

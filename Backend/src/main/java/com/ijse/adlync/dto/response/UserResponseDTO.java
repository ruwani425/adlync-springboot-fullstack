package com.ijse.adlync.dto.response;

import com.ijse.adlync.entity.enums.UserEntityRoleEnum;

public class UserResponseDTO {

    private Long id;
    private String password;
    private UserEntityRoleEnum role;
    private String name;
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

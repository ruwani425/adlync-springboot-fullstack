package com.ijse.adlync.dto.request;

import com.ijse.adlync.entity.enums.Advertisement_typeEntityTypeEnum;

public class Advertisement_typeRequestDTO {

    private Advertisement_typeEntityTypeEnum type;

    public Advertisement_typeEntityTypeEnum getType() {
        return type;
    }

    public void setType(Advertisement_typeEntityTypeEnum type) {
        this.type = type;
    }
}

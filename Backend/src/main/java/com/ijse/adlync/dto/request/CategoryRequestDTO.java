package com.ijse.adlync.dto.request;

import com.ijse.adlync.entity.enums.CategoryEntityNameEnum;

public class CategoryRequestDTO {

    private CategoryEntityNameEnum name;

    public CategoryEntityNameEnum getName() {
        return name;
    }

    public void setName(CategoryEntityNameEnum name) {
        this.name = name;
    }
}

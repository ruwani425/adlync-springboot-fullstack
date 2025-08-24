package com.ijse.adlync.dto.response;

import com.ijse.adlync.entity.enums.CategoryEntityNameEnum;

public class CategoryResponseDTO {

    private Long category_id;
    private CategoryEntityNameEnum name;

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    public CategoryEntityNameEnum getName() {
        return name;
    }

    public void setName(CategoryEntityNameEnum name) {
        this.name = name;
    }
}

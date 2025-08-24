package com.ijse.adlync.dto.response;

import com.ijse.adlync.entity.enums.Fashion_and_beautyEntityGenderEnum;

public class Fashion_and_beautyResponseDTO {

    private Long fashion_id;
    private String item_type;
    private String brand;
    private String size;
    private Fashion_and_beautyEntityGenderEnum gender;
    private String condition;

    public Long getFashion_id() {
        return fashion_id;
    }

    public void setFashion_id(Long fashion_id) {
        this.fashion_id = fashion_id;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Fashion_and_beautyEntityGenderEnum getGender() {
        return gender;
    }

    public void setGender(Fashion_and_beautyEntityGenderEnum gender) {
        this.gender = gender;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}

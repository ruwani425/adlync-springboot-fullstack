package com.ijse.adlync.dto.request;

import com.ijse.adlync.entity.enums.AgricultureEntityConditionEnum;

public class AgricultureRequestDTO {

    private String product_name;
    private Integer quantity;
    private String unit;
    private String season;
    private AgricultureEntityConditionEnum condition;

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public AgricultureEntityConditionEnum getCondition() {
        return condition;
    }

    public void setCondition(AgricultureEntityConditionEnum condition) {
        this.condition = condition;
    }
}

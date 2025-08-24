package com.ijse.adlync.dto.response;

import java.util.Date;

public class EssentialsResponseDTO {

    private Long essential_id;
    private String item_name;
    private String brand;
    private Integer quantity;
    private String unit;
    private Date expiry_date;

    public Long getEssential_id() {
        return essential_id;
    }

    public void setEssential_id(Long essential_id) {
        this.essential_id = essential_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public Date getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(Date expiry_date) {
        this.expiry_date = expiry_date;
    }
}

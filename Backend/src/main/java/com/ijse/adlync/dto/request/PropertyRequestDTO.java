package com.ijse.adlync.dto.request;

import com.ijse.adlync.entity.enums.PropertyEntityFurnishedEnum;
import com.ijse.adlync.entity.enums.PropertyEntityTypeEnum;

public class PropertyRequestDTO {

    private PropertyEntityTypeEnum type;
    private Double land_size;
    private String bedroom;
    private String barthroom;
    private PropertyEntityFurnishedEnum furnished;
    private String location_details;

    public PropertyEntityTypeEnum getType() {
        return type;
    }

    public void setType(PropertyEntityTypeEnum type) {
        this.type = type;
    }

    public Double getLand_size() {
        return land_size;
    }

    public void setLand_size(Double land_size) {
        this.land_size = land_size;
    }

    public String getBedroom() {
        return bedroom;
    }

    public void setBedroom(String bedroom) {
        this.bedroom = bedroom;
    }

    public String getBarthroom() {
        return barthroom;
    }

    public void setBarthroom(String barthroom) {
        this.barthroom = barthroom;
    }

    public PropertyEntityFurnishedEnum getFurnished() {
        return furnished;
    }

    public void setFurnished(PropertyEntityFurnishedEnum furnished) {
        this.furnished = furnished;
    }

    public String getLocation_details() {
        return location_details;
    }

    public void setLocation_details(String location_details) {
        this.location_details = location_details;
    }
}

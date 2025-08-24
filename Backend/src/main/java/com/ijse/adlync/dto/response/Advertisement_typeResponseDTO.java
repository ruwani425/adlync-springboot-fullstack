package com.ijse.adlync.dto.response;

import com.ijse.adlync.entity.enums.Advertisement_typeEntityTypeEnum;

public class Advertisement_typeResponseDTO {

    private Long ad_id;
    private Advertisement_typeEntityTypeEnum type;

    public Long getAd_id() {
        return ad_id;
    }

    public void setAd_id(Long ad_id) {
        this.ad_id = ad_id;
    }

    public Advertisement_typeEntityTypeEnum getType() {
        return type;
    }

    public void setType(Advertisement_typeEntityTypeEnum type) {
        this.type = type;
    }
}

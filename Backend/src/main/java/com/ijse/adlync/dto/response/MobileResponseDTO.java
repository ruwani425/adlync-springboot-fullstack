package com.ijse.adlync.dto.response;


public class MobileResponseDTO {

    private Long mobile_id;
    private String storage;
    private String condition;
    private String warranty;
    private String ram;
    private String brand;

    public Long getMobile_id() {
        return mobile_id;
    }

    public void setMobile_id(Long mobile_id) {
        this.mobile_id = mobile_id;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}

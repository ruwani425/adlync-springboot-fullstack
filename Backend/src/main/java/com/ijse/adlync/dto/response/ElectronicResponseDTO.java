package com.ijse.adlync.dto.response;


public class ElectronicResponseDTO {

    private Long electronic_id;
    private String brand;
    private String type;
    private String model;
    private String warranty;

    public Long getElectronic_id() {
        return electronic_id;
    }

    public void setElectronic_id(Long electronic_id) {
        this.electronic_id = electronic_id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }
}

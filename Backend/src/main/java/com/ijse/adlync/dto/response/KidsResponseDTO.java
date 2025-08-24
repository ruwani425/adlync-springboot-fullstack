package com.ijse.adlync.dto.response;


public class KidsResponseDTO {

    private Long kids_id;
    private String item_type;
    private String age_range;
    private String brand;
    private String condition;

    public Long getKids_id() {
        return kids_id;
    }

    public void setKids_id(Long kids_id) {
        this.kids_id = kids_id;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getAge_range() {
        return age_range;
    }

    public void setAge_range(String age_range) {
        this.age_range = age_range;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}

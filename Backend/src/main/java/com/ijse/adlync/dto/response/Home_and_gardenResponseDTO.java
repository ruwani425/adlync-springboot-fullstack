package com.ijse.adlync.dto.response;


public class Home_and_gardenResponseDTO {

    private Long home_garden_id;
    private String item_type;
    private String material;
    private String dimensions;
    private String condition;

    public Long getHome_garden_id() {
        return home_garden_id;
    }

    public void setHome_garden_id(Long home_garden_id) {
        this.home_garden_id = home_garden_id;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}

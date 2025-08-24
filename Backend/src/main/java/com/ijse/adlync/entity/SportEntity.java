package com.ijse.adlync.entity;

import jakarta.persistence.*;

@Entity
public class SportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long sport_id;

    private String equipment_type;

    private String brand;

    private String condition;

    private String size;

    @OneToOne(mappedBy = "sport")
    private PostEntity post;

    public Long getSport_id() {
        return sport_id;
    }

    public void setSport_id(Long sport_id) {
        this.sport_id = sport_id;
    }

    public String getEquipment_type() {
        return equipment_type;
    }

    public void setEquipment_type(String equipment_type) {
        this.equipment_type = equipment_type;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

}

package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.PropertyEntityFurnishedEnum;
import com.ijse.adlync.entity.enums.PropertyEntityTypeEnum;
import jakarta.persistence.*;

@Entity
public class PropertyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long property_id;

    @Enumerated(EnumType.STRING)
    private PropertyEntityTypeEnum type;

    private Double land_size;

    private String bedroom;

    private String barthroom;

    @Enumerated(EnumType.STRING)
    private PropertyEntityFurnishedEnum furnished;

    private String location_details;

    @OneToOne(mappedBy = "property")
    private PostEntity post;

    public Long getProperty_id() {
        return property_id;
    }

    public void setProperty_id(Long property_id) {
        this.property_id = property_id;
    }

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

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

}

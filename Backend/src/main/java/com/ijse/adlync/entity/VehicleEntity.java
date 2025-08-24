package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.VehicleEntityFuel_typeEnum;
import jakarta.persistence.*;
import java.util.Date;

@Entity
public class VehicleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long vehicle_id;

    private String mileage;

    private Date year;

    private String brand;

    private String model;

    @Enumerated(EnumType.STRING)
    private VehicleEntityFuel_typeEnum fuel_type;

    private String transmission;

    private String condition;

    @OneToOne(mappedBy = "vehicle")
    private PostEntity post;

    public Long getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(Long vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public Date getYear() {
        return year;
    }

    public void setYear(Date year) {
        this.year = year;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public VehicleEntityFuel_typeEnum getFuel_type() {
        return fuel_type;
    }

    public void setFuel_type(VehicleEntityFuel_typeEnum fuel_type) {
        this.fuel_type = fuel_type;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

}
